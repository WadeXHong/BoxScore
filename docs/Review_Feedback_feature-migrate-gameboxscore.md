# Code Review：`feature/migrate-gameboxscore`

審查日期：2026-10-04　審查者：獨立 reviewer（非作者）

---

## 1. 摘要

**範圍**
- 指定 base：`git merge-base login_page HEAD` = `39cceea`（2018-05），範圍內有 247 個 commit、399 個檔案。
- 不過 `login_page` 從 2018 年起就只多了 README 的 commit，所以這段範圍裡大部分是 2018 年 `master` 原有的歷史。真正的 2026 遷移工作是 **`master`（`ed55771`）..HEAD 的 38 個 commit、269 個檔案（+10746 / −530）**，本次審查以這段為主。
- 相對於 `master`，被刪除的只有 `app/build.gradle`、`build.gradle`、`settings.gradle`（已改成 `.kts`）。legacy Java 都還在 `legacy/` 或 `Legacy*`，**符合「不刪 legacy」規則**。
- **建議 PR 的目標分支改成 `master`。** 若以 `login_page` 為目標，會一起帶進約 200 個 2018 年的 commit，reviewer 很難看出實際的變更。

**建置與測試（實際執行）**
`./gradlew :app:testDebugUnitTest :app:assembleDebug` 成功（exit 0）。共執行 48 個測試，0 失敗，數量與 tracker 的說法相符。

**整體評估**
架構方向正確：以事件紀錄（event log）為資料來源、計分抽成純函式、Firebase 對應也抽成純函式並有測試，可讀性也好。但資料層有幾個會造成**資料遺失或重複**的問題，目前沒有測試覆蓋，tracker 對其中部分問題的描述也偏輕。另外還有一個每次冷啟動都會出現的設定回歸（亮度）。

**是否可合併：尚不建議。** 至少要先處理下方「阻擋合併」的項目。若只合併到開發分支、暫不發佈，「應修正」的項目可以排進下一輪，但發佈前必須完成。

---

## 2. 發現

### 阻擋合併

#### B1. `TEMP MOCK DATA SCRIPT` 仍在，而且會在登出／刪光球隊後重新產生資料（發佈阻擋）— 已驗證
- `app/src/main/java/com/wadexhong/boxscore/MainActivity.kt:63-94`
- 依指示，這裡只標記為 release blocker。補充一個行為事實，方便模擬器測試時判讀結果：這段用 `GlobalScope` 持續 `collect` `getAllTeams()`，只要球隊表變成空的就會再插入「Test Team」，例如刪除最後一支球隊、或登出後 `clearAllTables()`。因此 checklist 中「登出後資料由雲端重新下載」「刪除球隊」這兩項的結果會被它干擾。這些 Test Team 不會上傳到 Firebase。
- 建議：發佈前移除，或至少用 `BuildConfig.DEBUG` 包起來，並改成只執行一次。

#### B2. Room 表沒有任何唯一鍵，啟動時的兩條匯入流程可能產生重複的比賽與加倍的數據 — 程式碼已驗證／時序為推測
- `database/entity/GameInfoEntity.kt:8-12`、`TeamInfoEntity`、`TeamPlayerEntity`：四張表的 primary key 都是自增 `_id`。`app/schemas/.../2.json` 中 `indices` 全是空的，`game_id`、`team_id`、`player_id` 都沒有 unique。
- `@Insert(onConflict = REPLACE)` 搭配 `id = 0` 時永遠會插入新列，REPLACE 不會生效。
- 有兩條流程都採用「先查不存在、再插入」，而且彼此並行：
  - `BoxScore.onCreate` → `LegacyDataMigrator.migrateIfNeeded`（`database/LegacyDataMigrator.kt:28`，`Dispatchers.IO`）
  - `MainActivity.onCreate` → `FirebaseSync.download` → `RemoteDataImporter.import`（`sync/RemoteDataImporter.kt:25-36`）
- **失敗情境**：legacy 使用者升級後第一次啟動，且已登入，這正是遷移的主要對象。
  1. migrator 執行 `getGameInfoById(X) == null`（`LegacyDataMigrator.kt:168`）。
  2. 此時 Firebase 快照回來，importer 也判定 X 不存在，插入 X 的 info 和 rows。
  3. migrator 接著插入 X 的 info，而且 X 不在 `existingGameIds` 裡，所以 legacy 的 game_data 也會被插入（`:224`）。
  - 結果：歷史列表出現兩筆 X；`game_data` 有兩份，`HistoryStats.sum`／`totals` 算出的球員與全隊數據都會**加倍**；Excel 匯出也會重複。
  - 球隊和球員也可能重複，StartGame 會列出兩支同名球隊。
- 測試沒有抓到這個問題：`testutil/FakeGameStore.kt:23-27` 的 `insertGameInfo` 會依 `gameId` 去重，`FakeDaos.kt:28-30` 的 `insertTeam` 會依 `teamId` 去重。也就是說，fake 的語意比真實 Room 還嚴格，把這個缺陷遮住了。
- **建議**：
  1. 在 `game_info.game_id`、`team_info.team_id`、`team_player.player_id` 加 unique index，`game_data` 可以加 `(game_id, player_id, quarter)`。依專案規則，這需要 bump 版本並寫 `Migration`（先去重再建 index）。
  2. 讓 import 和 migration 互斥：例如 download 先 `join` migration 的 Job，或兩者共用一個 `Mutex`；並把 check+insert 包進 `withTransaction`。
  3. 讓 fake DAO 的語意與真實 Room 一致，或加上 Room in-memory 的 instrumented/Robolectric 測試。

### 應修正

#### S1. 續賽一場升級前的 legacy 未完成比賽後，只要再記錄一筆數據，原有的球員數據就會全部被覆蓋 — 已驗證
- `database/GameRepository.kt:45-64`（`:49` 只有在事件為空時才保留，`:58` `replaceGameData` 只用事件重算）
- `GameBoxScoreViewModel.kt:75-79`：比分、節數、團犯都只由事件計算。
- **失敗情境**：舊版 app 打到第 3 節，game_data 已有 40 分的數據，此時升級。
  1. Dashboard 偵測到 `is_gameover=0`，選擇續賽。
  2. 畫面顯示 0:0、第 1 節。舊版的比分和節數存在 legacy SharedPreferences（`YOUR_TEAM_TOTAL_SCORE`、`QUARTER`），並沒有被遷移。
  3. 使用者記一筆 2PM，按返回後選儲存。
  4. `saveProgress` 發現事件非空，以「只有 1 筆事件」重算並取代全部 `game_data`，`your_score` 變成 2。原本 40 分的球員數據永久消失，之後結束比賽時還會把這個結果上傳到 Firebase。
- tracker「已知限制 3」只寫到「只要不再記錄新事件……會保留」，沒有寫出「記錄任何事件就會清掉舊數據」。使用者不會知道這個條件，實際上就是資料遺失。
- **建議（擇一）**：
  - (a) 載入時若「無事件且 game_data 有非零數據」，就把 legacy 數據轉成基準事件。可由 fgm/fga/tpm/tpa/ftm/fta/oreb/... 反推。pf/tov 的進攻／防守拆分無法還原，請明確記錄取捨。
  - (b) 對這類比賽不提供續賽，只提供「結束並儲存／放棄」。
  - 無論選哪一種，都要補一個「legacy 數據 + 新事件」的測試。

#### S2. 新版 Settings 調整的亮度在 app 重啟後失效；legacy 設定在 DataStore 遷移後被移除 — 已驗證
- `setting/SettingsRepository.kt:11-16`：`SharedPreferencesMigration(context, context.packageName)`，遷移的正是 legacy 的 prefs 檔（`modelhelper/SharedPreferenceHelper.java:72` 以 packageName 開檔）。
- `BoxScore.java:37`：冷啟動時仍從 **legacy SharedPreferences** 讀取 `sBrightness`。
- `setting/SettingActivity.kt:36`：只有打開 Settings 時，才會從 DataStore 把值寫回 `sBrightness`。
- **失敗情境**：在新版 Settings 把亮度設為 0.3，關掉 app 再重開。`BoxScore.onCreate` 從 SharedPreferences 讀到的不是 0.3：新值只寫進 DataStore；而且 `SharedPreferencesMigration` 預設會刪除已遷移的 key，所以讀到的是 −1。結果 Dashboard、StartGame、GameBoxScore 都回到系統亮度，直到使用者再打開一次 Settings。
- **建議**：在 `BoxScore.onCreate`（或第一個 Activity）從 DataStore 讀亮度，例如用 `runBlocking { dataStore.data.first() }` 或非同步更新後再套用；不要再讀 legacy prefs。

#### S3. LegacyDataMigrator 失敗後重試，會讓先前已插入的比賽永遠缺少 game_data — 邏輯已驗證／觸發條件推測
- `database/LegacyDataMigrator.kt:28-43, 128-174, 224`
- 整個遷移沒有包在 transaction 裡，只有全部成功才設定 flag。重試時，上次已插入的 game_info 會被當成「已存在（例如來自雲端）」，放進 `existingGameIds`，於是這些比賽的 legacy game_data **永遠不會再插入**。
- 可能的觸發點：
  - 程序在遷移途中被殺掉，例如第一次啟動時使用者馬上滑掉 app。
  - 任一欄位為 NULL。`cursor.getString()` 回傳的平台型別傳進非 null 的 Kotlin 建構參數時會丟出 NPE。legacy `OnCreateDataBaseTask.java:61-74` 會把 Firebase 缺少的欄位以 null 寫進 SQLite，因此 legacy DB 確實可能有 NULL（推測）。若是後者，每次啟動都會在同一列失敗，game_data 永遠無法完整遷移。
- **建議**：用 `db.withTransaction { ... }` 包住整個遷移（全有或全無）；以 `getString(...) ?: ""` 處理 NULL；`existingGameIds` 只放「遷移開始前就已存在」的 id。

#### S4. 登出與進行中的雲端下載之間有競態，可能把前一位使用者的資料寫回本機 — 程式碼已驗證／時序推測
- `setting/SettingsViewModel.kt:73-84` 會 `clearAllTables()` 後再登出。
- `MainActivity.kt:153-155` 的 `syncFromCloud()` 跑在 `MainActivity.lifecycleScope` 上。MainActivity 在背景時沒有被銷毀，所以下載不會被取消，而且 `RemoteDataImporter.import` 沒有檢查 uid 是否還有效。
- **失敗情境**：網路慢時，啟動後立刻進 Settings 登出。`clearAllTables()` 完成後，A 的快照才回來並寫入 Room。接著 B 登入，B 的下載只會刪除「B 雲端沒有的已結束比賽」，所以 A 的**球隊與球員會留在 B 的裝置上**，A 的未完成比賽也可能留下。這是隱私問題，也會造成資料混雜。
- **建議**：登出前取消下載 Job，或在 `import` 前後比對 `currentUser?.uid` 是否等於開始下載時的 uid，並在 transaction 內完成寫入。

#### S5. `saveProgress` 用「事件是否為空」判斷是否為 legacy 比賽，undo 到零筆時會留下過期的數據 — 已驗證
- `database/GameRepository.kt:49`
- **失敗情境**：新比賽記一筆 2PM，按返回並儲存，此時 game_data 與 your_score=2 已寫入。續賽後把那筆 undo 掉（events 變成空），再按結束比賽。`saveProgress` 直接回傳舊的 info，結束的比賽仍然記為 2 分，球員也還有 2 分，而且會上傳到 Firebase。
- **建議**：「是不是 legacy 比賽」應該用明確的狀態來判斷，例如 S1 的基準事件化、或只在載入時判定一次，而不是用 `events.isEmpty()` 推測。修好 S1 (a) 後，這個問題通常會一起消失。

### 建議

#### N1. 開賽寫入沒有 transaction — 已驗證
`GameRepository.kt:28-34` 先 `insertGameInfo` 再 `insertAllGameData`，兩者分開執行。若程序在兩步之間死掉，會留下一場可續賽但沒有名單的比賽。這時 `loadGameInfo` 拿到空名單，ViewModel 的 `rosterFlow()`（`GameBoxScoreViewModel.kt:83-88`）會退回整隊名單，而且用未排序 query 的前 5 人當先發，而不是使用者選的先發。`discardGame`（`:79-82`）也一樣。建議把這兩組操作都改成 `@Transaction` 的 DAO 方法或 `withTransaction`。

#### N2. 雲端下載會刪掉雲端沒有的已結束比賽，而且未啟用離線持久化 — 已驗證（tracker 已記錄，與 legacy 同風險）
`RemoteDataImporter.kt:20-23`；專案中沒有 `setPersistenceEnabled(true)`，`updateChildren` 的離線寫入只存在記憶體。情境：離線時結束比賽，app 被殺掉，寫入跟著消失；下次上線啟動時，本機這場比賽會被刪除。若雲端節點為空（`parseUser(null)` → 空的 `RemoteData`），則會刪掉**所有**本機已結束的比賽；legacy 在這種情況會因為 `hashMap` 為 null 而 NPE，反而不會刪。建議：啟用 Realtime Database 持久化；並且只刪除「確定曾經上傳成功」的比賽（例如用一個 synced 欄位，需要 Migration），或至少在 remote 的 `game_info` 節點不存在時跳過刪除。

#### N3. 刪除球隊後，比賽的上傳／刪除仍會寫 `team_info/{teamId}/team_history_amount` — 已驗證（legacy 行為相同）
`FirebaseMapper.kt:62`、`FirebaseSync.kt:50`。若球隊已被刪除，雲端會出現只有 history 欄位的殘缺節點，下次下載時會變成一支名稱空白的球隊。這和 legacy `Create.updateTeamHistoryAmount` 的行為相同，可以留待後續處理，但值得列入已知限制。

#### N4. `GameBoxScoreViewModel` 直接依賴具體的 `BoxScoreDatabase` — 已驗證
因此 GameBoxScore 最核心的 ViewModel（記錄、undo、換人、存檔／結束的串接）完全沒有測試。建議把建構參數改成 `GameEventDao`、`PlayerDao`、`GameRepository`，就能沿用現有的 `FakeGameStore` 補上測試。

#### N5. CreateTeam 對話框每開一次就留下一個 ViewModel — 已驗證（輕微）
`TeamManageActivity.kt:184-187` 用 `key = "createTeam-$session"`，ViewModel 綁在 Activity 的 store 上，每開一次就多一個，直到 Activity 結束才釋放。影響很小，可以改成以 NavHost 的 dialog destination 承載。

---

## 3. 測試評估

**有涵蓋（48 個，全數通過）**
- `GameStateCalculatorTest`（12）：得分、對手加減分、團犯只算當節、進攻犯規不算團犯、補登不影響當節團犯、換人鏈結與無效配對、每人每節 4 列的彙總規則。這部分寫得好，規則也對照了 legacy `plusData` 的欄位語意。我逐項核對了 legacy `GameDataDbHelper.plusData`：2PM、3PM、FT、3PA、進攻／防守犯規的欄位加總都一致。
- `GameRepositoryTest`（9）：開賽冪等、存檔彙總、結束、放棄、刪除、續賽載入名單順序、legacy 無事件時保留數據。
- `FirebaseSyncTest`（5）：上傳格式、quarter 以 List 回傳的情況、round-trip、匯入合併與刪除。
- `GameHistoryTest`（8）、`StartGameViewModelTest`（10）、`PlayerInputRulesTest`（3）。

**主要缺口**
1. **Fake DAO 的語意與 Room 不一致**（`FakeGameStore.insertGameInfo`、`FakeTeamStore.insertTeam` 會去重），導致 B2 無法被測出。所有 SQL（排序、`countFinishedGames`、`syncAllTeamPlayersAmounts` 的子查詢）也完全沒有在真實 SQLite 上驗證過。建議加上 Robolectric 或 instrumented 的 Room in-memory DAO 測試，以及 `MigrationTestHelper`，替之後的 v3 做準備。
2. **`LegacyDataMigrator` 沒有任何測試**，包括重試、NULL 欄位、與 importer 並行的情況（S3、B2）。
3. **`GameBoxScoreViewModel` 沒有測試**（N4）。
4. **沒有「legacy 數據 + 新事件」的測試**（S1），也沒有「undo 到零事件後結束比賽」的測試（S5）。
5. 設定／DataStore 遷移與亮度沒有測試（S2）。
6. `ExampleUnitTest`（`com.example.wade8`）是樣板，可以移除。

---

## 4. 與 legacy 行為差異

| 差異 | 位置 | 是否刻意／有文件 |
| :--- | :--- | :--- |
| legacy 進行中比賽的比分／節數／團犯（存在 SharedPreferences）沒有遷移，續賽時顯示 0、第 1 節；記錄新事件會清掉舊數據 | `GameRepository.saveProgress`、ViewModel | 只有部分寫在文件裡（tracker 限制 3），**低估了資料遺失的範圍**（S1） |
| 亮度在冷啟動後不會套用新設定 | `BoxScore.java:37` | 非刻意（S2） |
| Undo 紀錄改為持久化，續賽後仍可 undo／編輯（legacy 只存在記憶體，續賽後就清空） | `game_event` | 合理的改進，tracker 沒有特別寫出 |
| 下載同步：legacy 的 `OnCreateDataBaseTask.java:40-47` 用 `hashMap.containsKey(gameId)` 檢查 `users/{uid}` 的頂層 key，實際上每次都把本機已結束的比賽全部刪掉再從雲端補回；新版只刪除雲端沒有的比賽。雲端為空時，legacy 會 NPE（不刪），新版會全部刪掉 | `RemoteDataImporter` | 新版語意比 legacy 合理；「雲端為空時全部刪除」沒有寫進文件（N2） |
| 下載的比賽，`game_data` 依背號排序後插入（`FirebaseMapper.kt:174`） | | 只影響已結束的比賽，無實際影響 |
| 分享 .xls 改存 app cache；歷史球員依背號的數字排序 | | 有文件，屬刻意調整 |
| 隊伍犯規上限：手動「我方團犯 +1」會被 `maxFoul` 擋住，但球員的防守犯規不會 | `GameBoxScoreViewModel.kt:137-143` | 與 legacy 一致（legacy 也有 `//TODO 犯滿畢業`） |
| 節數不能往回調 | | 與 legacy 一致（legacy 的 `setOnLongClick()` 已被註解掉） |

---

## 5. 優點

- 計分改為事件溯源，再由純函式 `GameStateCalculator` 重算。程序死掉後可以完整還原比分、節數、場上名單，這是相對 legacy（散落在 SharedPreferences 與記憶體中）的實質改善。我也核對了它與 legacy `plusData` 的欄位規則，結果一致。
- `FirebaseMapper` 把 Firebase 的格式轉換抽成純函式，並處理了 quarter 鍵被 Firebase 轉成 List 的陷阱，測試也有涵蓋。
- 已匯出 schema，`fallbackToDestructiveMigrationFrom(1)` 只套用在從未發佈的 v1；`FirebaseSync` 在登出狀態下全部是 no-op，避免了 legacy `getUid()` 為 null 時的崩潰。
- legacy 程式碼完整保留在 `legacy/`，相對於 `master` 沒有刪除任何 legacy 檔案。

---

## 6. 處理結果（作者回覆，2026-10-04）

| 項目 | 處理 | Commit |
| :--- | :--- | :--- |
| B1 | mock data 依使用者要求保留供模擬器測試，但改為只在 debuggable build 執行、每次啟動只檢查一次（不再持續監聽並重新產生）。發佈前仍需移除 | `8bc1bc4` |
| B2 | Room v3：`game_id`／`team_id`／`player_id` 加 unique index，`MIGRATION_2_3` 先去重再建 index，並有測試比對匯出的 v3 schema；`DataWriteLock` 讓 legacy 遷移、雲端下載、登出清除互斥，遷移與匯入各自包在 transaction；fake player DAO 改為與 REPLACE 語意一致 | `f42793c` |
| S1 | 新比賽開賽時寫入 `GAME_START` 事件；legacy 比賽（沒有任何事件）在續賽或存檔前，會把 game_data 與對手比分轉成可重播回相同數據的基準事件，節數取有數據的最後一節。新事件疊加在上面，不再覆蓋 | `29c489d` |
| S2 | 冷啟動改從 DataStore 讀亮度 | `26a14b0` |
| S3 | 遷移包在 transaction（全有或全無）；NULL 欄位以空字串／0 處理 | `f42793c` |
| S4 | 登出先 sign out 再清除，且在 `DataWriteLock` 內執行；下載寫入前確認 uid 未變 | `f42793c` |
| S5 | 改以 `GAME_START` 標記判斷，不再用「事件為空」推測；已加 undo 到零後結束比賽的測試 | `29c489d` |
| N1 | `GameRepository` 的開賽／存檔／結束／放棄／刪除都在同一個 transaction | `29c489d` |
| N2 | 啟用 Realtime Database 磁碟持久化；雲端完全沒有比賽時不刪除本機歷史 | `7016b14` |
| N3 | 未修改（與 legacy 相同），已列入 tracker 已知限制 | — |
| N4 | ViewModel 改為注入 DAO 與 `GameRepository`，新增 10 個測試 | `addbc46` |
| N5 | 新增球隊改為 navigation dialog destination，關閉即釋放 ViewModel | `8bc1bc4` |

S1 的取捨：legacy 總數無法區分進攻／防守犯規，犯規一律轉成防守犯規、失誤轉成一般失誤（`PF`、`TOV` 欄位結果相同，但續賽當節的團隊犯規可能偏高）；對方團隊犯規無法還原。

單元測試由 48 個增加到 65 個，全部通過，`assembleDebug` 成功。仍未在實機／模擬器上驗證；Room 的 SQL 仍沒有在真實 SQLite 上執行的測試。


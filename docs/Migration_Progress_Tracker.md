# BoxScore Migration Progress Tracker

> 最後更新：2026-10-04（branch `feature/migrate-gameboxscore`）

這張表格從兩個維度（**By Pages 垂直切片** 與 **By Architecture 架構分層**）來展示我們目前的重構進度。
根據我們的計畫，理想的推動方向是 **「水平向右（把單一功能的架構全部升級完）」** 再 **「垂直向下（推進到下一個功能）」**。

圖例：✅ 完成　⚠️ 部分完成（見備註）　❌ 尚未開始　➖ 決定略過

| 模組 (Pages) | 1. 轉為 Kotlin | 2. Data Layer (Room/Flow) | 3. Domain Layer (Use Cases) | 4. Presentation (MVVM) | 5. UI Layer (Compose) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **球隊管理 (TeamManage)** | | | | | |
| 　- 首頁 (Team Main) | ✅ | ✅ | ➖ | ✅ | ✅ |
| 　- 球員列表 (Team Players) | ✅ | ✅ | ➖ | ✅ | ✅ |
| 　- 新增球隊/球員 (Create...) | ✅ | ✅ | ➖ | ✅ | ✅ |
| 　- 外殼 (TeamManageActivity) | ❌ (Java) | ➖ | ➖ | ❌ (MVP) | ⚠️ Fragment + ComposeView |
| **主選單與認證 (Dashboard)** | ✅ | ⚠️ 續賽仍讀舊 SQLite | ➖ | ✅ | ✅ |
| **開始比賽 (StartGame)** | ✅ | ✅ | ➖ | ✅ | ✅ |
| **即時數據 (GameBoxScore)** | ✅ | ⚠️ 僅寫 Room `game_event` | ➖ | ✅ | ✅ |
| **歷史紀錄 (GameHistory)** | ❌ | ❌ (舊 SQLite) | ❌ | ❌ (MVP) | ❌ (XML) |
| **設定 (Settings)** | ✅ | ✅ DataStore（刪除資料仍呼叫舊 DbHelper） | ➖ | ✅ | ✅ |

**Domain Layer**：目前各模組由 ViewModel 直接呼叫 DAO，未建立 Use Case 層；Hilt 也已略過（改用手動 ViewModel Factory）。如未來需要再補。

舊實作依「Never delete legacy code」規則保留於 `legacy/` package。

---

## Migration Review 修正（`implementation_plan.md`，依 `MigrationReviewReport.html`）

| Section | 狀態 | 重點 |
| :--- | :---: | :--- |
| 1. Root / Auth | ⚠️ | 修正編譯錯誤、Loading 遮罩阻擋觸控、亮度 / 系統列。Debounce 僅套用於 Dashboard 與 Login，尚未涵蓋所有互動元件 |
| 2. GameBoxScore | ✅ | 多指滑動手勢、補登、復原紀錄編輯 / 刪除、換人持久化（`SUBSTITUTION_OUT/IN` 事件）、每節犯規歸零、犯規上限、節數上限、離開 / 結束比賽對話框、設定頁入口、亮度 |
| 3. Settings | ✅ | （Gemini 完成） |
| 4. StartGame | ✅ | 返回確認、無合法球隊對話框、可滑動但會彈回第一個未完成頁、數值上下限與 Toast、輸入去除前導空白、Quick game fallback、修正 players collector 洩漏 |
| 5. TeamManage | ✅ | 展開箭頭旋轉動畫、刪除對話框帶名稱、刪除球隊連同球員、`playersAmount` 同步（含一次性修復）、新增球員即時驗證（背號重複 / 前導 0 / 長度） |

### 與 Review Report 不同、改以 legacy 行為為準的項目

- StartGame 數值上限：報告寫「節數 9、暫停最少 0」，legacy 實際為節數 1–4、暫停 1–9、犯規 1–9、每節 1–40 分鐘。
- 球員詳細頁導航：legacy `transToPlayerDetail` 為空實作，只保留點擊 hook。
- 球員列表編輯圖示：legacy ImageView 無 src，保留相同佔位空間。
- 復原紀錄的「標記」選項：legacy 無作用，未移植。
- `strings.xml` 的 `max_foul_message` / `min_foul_message` 文字在 legacy 即互換，已修正（legacy 畫面同受影響）。

---

## 已知缺口 / 待決策

1. **資料孤島**：新 GameBoxScore 只寫 Room `game_event`，未寫 `game_info` / `game_data`。因此：
   - 結束比賽不會標記比賽結束、不更新球隊歷史場數、不上傳 Firebase（`GameBoxScoreActivity` 內有 TODO）。
   - Dashboard 續賽仍從舊 SQLite 讀取，傳入的 GameInfo 為空。
   - GameHistory（仍為舊 SQLite）看不到新比賽。
2. **Firebase 同步**：新 TeamManage 不再同步球隊 / 球員到 Firebase。
3. **Room 遷移**：DB version 2 使用 `fallbackToDestructiveMigration`，未來升版會清空資料。
4. **測試**：目前只有樣板測試（ExampleUnitTest / ExampleInstrumentedTest），上述修正尚未在實機 / 模擬器驗證。
5. **測試資料**：`MainActivity` 內的 TEMP MOCK DATA SCRIPT 為模擬器測試用，正式發佈前需移除。

## 建議下一步

1. 決定 GameBoxScore ↔ 舊 DB 的資料層方案（寫回舊表 or 將 GameHistory / Dashboard 續賽改讀 Room）。
2. 依決策遷移 GameHistory 模組。
3. TeamManageActivity 外殼轉 Kotlin / Compose Navigation，補回 Firebase 同步。

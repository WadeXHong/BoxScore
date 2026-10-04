# BoxScore Migration Progress Tracker

> 最後更新：2026-10-04（branch `feature/migrate-gameboxscore`）

這張表格從兩個維度（**By Pages 垂直切片** 與 **By Architecture 架構分層**）來展示我們目前的重構進度。
根據我們的計畫，理想的推動方向是 **「水平向右（把單一功能的架構全部升級完）」** 再 **「垂直向下（推進到下一個功能）」**。

圖例：✅ 完成　⚠️ 部分完成（見備註）　❌ 尚未開始　➖ 決定略過

| 模組 (Pages) | 1. 轉為 Kotlin | 2. Data Layer (Room/Flow) | 3. Domain Layer (Use Cases) | 4. Presentation (MVVM) | 5. UI Layer (Compose) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **球隊管理 (TeamManage)** | ✅ | ✅ | ➖ | ✅ | ✅ Navigation Compose |
| **主選單與認證 (Dashboard)** | ✅ | ✅ 續賽讀 Room | ➖ | ✅ | ✅ |
| **開始比賽 (StartGame)** | ✅ | ✅ | ➖ | ✅ | ✅ |
| **即時數據 (GameBoxScore)** | ✅ | ✅ event log + `GameRepository` | ➖ | ✅ | ✅ |
| **歷史紀錄 (GameHistory)** | ✅ | ✅ | ➖ | ✅ | ✅ Navigation Compose |
| **設定 (Settings)** | ✅ | ✅ DataStore | ➖ | ✅ | ✅ |
| **雲端同步 (Firebase)** | ✅ `sync/FirebaseSync` | ✅ 以 Room 為資料來源 | ➖ | — | — |

**Domain Layer**：ViewModel 直接使用 DAO / `GameRepository`，未另建 Use Case 層；Hilt 已略過（手動 ViewModel Factory）。

舊實作依「Never delete legacy code」規則保留：`legacy/` package、`LegacyGameBoxScoreActivity`、`LegacyTeamManageActivity`（含其 Fragments）、`legacy/gamehistory`。舊的 SQLite helper 只剩登出清除與一次性 `LegacyDataMigrator` 會用到。

---

## 資料層（Room 為唯一資料來源）

| 資料 | 位置 | 說明 |
| :--- | :--- | :--- |
| 比賽進行中的紀錄 | `game_event` | 事件溯源：比分、犯規、節數、上場名單都由事件重算（`GameStateCalculator`） |
| 比賽設定與結果 | `game_info` | 開賽時寫入，`is_gameover` 標記是否結束 |
| 球員數據 | `game_data` | 與 legacy 相同：每位球員每節一列；同時是該場名單（先發在前）。存檔／結束時由事件彙總 |
| 球隊／球員 | `team_info` / `team_player` | `players_amount`、`history_amount` 由 DAO 同步 |

- 比賽生命週期：`GameRepository`（開賽、續賽載入、存檔、結束、放棄、刪除歷史）。
- Schema 匯出至 `app/schemas`；只有開發版的 v1 允許破壞性遷移，之後任何 schema 變更都必須寫 `Migration`。
- Firebase：`FirebaseSync`（上傳結束的比賽、刪除、球隊/球員增刪、Excel 上傳、登入後下載），節點格式與 legacy 相同。

---

## 2026-10-04 完成項目

| Step | Commit | 內容 |
| :--- | :--- | :--- |
| 0 | `d403a73` `d6c8586` | 單元測試基礎；計分邏輯抽成純函式；進攻犯規不算團犯、PF 先選犯規類型（legacy 行為） |
| 1 | `020bbcf` | 移除破壞性遷移、匯出 schema；登出同時清 Room；登出後回登入畫面 |
| 2 | `866bfdf` | `GameRepository`；開賽寫入 Room |
| 3 | `e0b8325` | 結束比賽結算、返回存檔／放棄；比賽中數據統計表（原本全是 0） |
| 4 | `61cba53` | Dashboard 續賽改讀 Room，並帶入真正的比賽設定與名單 |
| 5 | `485017b` | GameHistory 遷移（列表、球隊數據圖表、球員數據篩選、分享 .xls、刪除） |
| 6 | `9d0ba7f` | Firebase 同步改以 Room 為來源 |
| 7 | `7ad1df6` | TeamManage 外殼改為 Compose NavHost；防連點補齊並修正競態 |

單元測試：48 個（`./gradlew :app:testDebugUnitTest`），涵蓋計分/換人/犯規、球員數據彙總、`GameRepository`、StartGame 驗證、新增球員輸入規則、歷史統計與分享、Firebase 格式轉換與下載合併。

### 與 legacy 刻意不同之處

- 分享 .xls 存到 app cache，不再需要儲存權限（新版 Android 已無法寫入 legacy 的外部儲存路徑）。
- 歷史球員數據依背號「數字」排序（legacy 以字串排序，10 會排在 2 前面）。
- StartGame 數值上限、球員詳細頁、編輯圖示、「標記」選項等依 legacy 實際行為（詳見 `implementation_plan.md` 的處理紀錄）。

---

## 已知限制 / 待確認

1. **未在實機／模擬器驗證**：以上皆通過編譯與單元測試，但 UI、Firebase 讀寫需實際操作確認（見 `docs/Simulator_Test_Checklist.md`）。
2. **下載同步會移除雲端沒有的已結束比賽**（沿用 legacy 規則）。若某場比賽上傳失敗，下次啟動時本機紀錄會被移除。
3. **舊版進行中的比賽**：升級前由舊 app 開始、尚未結束的比賽，續賽時比分顯示為 0（舊資料沒有事件紀錄）；但只要不再記錄新事件，結束時會保留原本的球員數據。
4. **測試資料**：`MainActivity` 的 TEMP MOCK DATA SCRIPT 為模擬器測試用，正式發佈前需移除。
5. **Domain Layer / Hilt**：未實作，如有需要再補。

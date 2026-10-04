# Phase 2.2: Global Room Database Setup Plan

## Objective
目前專案使用的是 Android 傳統的 `SQLiteOpenHelper` (散落在 `GameDataDbHelper`、`GameInfoDbHelper`、`TeamDbHelper` 中)，並透過字串拼接執行 SQL 語法。這種做法容易出錯、無法在編譯期檢查語法，且難以結合現代 UI 框架進行響應式資料流 (Reactive Stream) 更新。

本計畫旨在將老舊的 SQLite 實作遷移至現代化的 **Jetpack Room** 架構，結合 Kotlin Data Class 與 Coroutine `Flow`，為之後的架構升級 (MVVM) 打造最穩固的資料底層。

## Step 1: 匯入依賴與 KSP 插件
在 `libs.versions.toml` 與 `build.gradle.kts` 加上 Room 的套件庫：
*   **KSP Plugin** (Kotlin Symbol Processing)：取代舊的 KAPT，用來加速 Room 的編譯期檢查與程式碼生成。
*   **Room Runtime & KTX**：Room 核心庫與對 Kotlin Coroutines/Flow 的原生支援。
*   **Room Compiler**：透過 KSP 處理 `@Entity`、`@Dao` 標註。

## Step 2: 建立 Entities (資料表模型)
根據目前的 SQLite 架構，我們將原本的 Contract 轉換為 Kotlin Data Class：
1.  **`GameInfoEntity`**：對應 `GameInfoDBContract.TABLE_NAME` (記錄比賽的 MetaData，如日期、對戰隊伍、比分)。
2.  **`GameDataEntity`**：對應 `GameDataDBContract.TABLE_NAME` (記錄詳細的 Play-by-play 數據或單節數據)。
3.  **`TeamInfoEntity`**：對應 `TeamInfoDBContract.TABLE_NAME` (記錄球隊的名稱等基本資料)。
4.  **`TeamPlayerEntity`**：對應 `TeamPlayersContract.TABLE_NAME` (記錄特定球隊底下的球員資訊：名字、背號、位置)。

*所有的欄位名稱將與原先的 SQLite DB 欄位保持一致，以方便後續的資料轉移 (Migration)。*

## Step 3: 建立 DAOs (Data Access Objects)
不再需要手寫 `Cursor` 或是 `ContentValues`，我們將建立三個主要的 DAO 介面：
1.  **`TeamDao`**：提供 `getAllTeams(): Flow<List<TeamInfoEntity>>`、`insertTeam()` 等。
2.  **`PlayerDao`**：提供 `getPlayersByTeamId(teamId): Flow<List<TeamPlayerEntity>>` 等。
3.  **`GameDao`**：提供比賽歷史紀錄與詳細數據的存取。

> **亮點**：回傳型態將全面採用 `Flow`，這代表未來只要資料庫一有變動，Jetpack Compose 畫面就會「自動」更新，不需手動 Call API 重新撈資料。

## Step 4: 建立 RoomDatabase 核心與 Migration 策略
建立 `BoxScoreDatabase.kt` 繼承 `RoomDatabase`。
*   **資料遷移策略 (Migration)**：
    由於舊版 App 已經在手機上建立了 SQLite，我們有兩個選擇：
    1.  **Destructive Migration (開發期推薦)**：直接清除舊有開發資料，建立乾淨的 Room 表格。
    2.  **Seamless Migration (正式環境必須)**：撰寫 SQL Migration 語法，把舊有 `.db` 的資料無縫轉移到 Room 架構中。
    *(在執行此計畫時，我會先與您確認要採用哪一種策略)*

## Step 5: Provider (提供 Database 實例)
由於我們先跳過了 Hilt，為了避免在各個 Activity/Fragment 重複實例化資料庫，我們將會在 `BoxScore.java` (Application 類別) 或是建立一個單例的 `DatabaseProvider` 來全局提供 DAO 的實例，供未來的 Repository 層呼叫。

---

### 下一步行動 (Action Items)
如果確認此計畫可行，我們將依照以下順序開始執行程式碼修改：
1.  設定 `libs.versions.toml` 與 Gradle。
2.  建立 `database/entity` 與 `database/dao` 資料夾，並實作上述的 Kotlin 檔案。
3.  把 Application 接上 Room Database，確保可以順利編譯通過。

# Migration Plan: Game History

## 1. Business Logic & Existing Behavior Analysis
`GameHistoryActivity` features a classic Master-Detail flow, allowing users to browse past games and dive deep into their post-game analytics.

### Interface Layout & Interaction Logic
*   **Master View: History List (`HistoryMainFragment`)**
    *   Features an orange-colored Toolbar.
    *   Displays a list with column headers (Date/Title, Your Team, Score, Opponent).
    *   Each row shows the game info, with a "More" (`更多`) CTA text on the far right that triggers a Share/Export Intent.
    *   Tapping the entire row navigates to the Detail view.
*   **Detail View: Post-Game Analytics (`HistoryDetailFragment`)**
    *   Uses a `TabLayout` and a `NonSwipeableViewPager` to divide the analytics into two categories:
    *   **Tab 1 - Team Data**:
        *   Provides a side-by-side comparison of "Your Team" vs "Opponent".
        *   The legacy code used custom RecyclerView items (`item_history_teamdata_chart.xml`) to draw Bar Charts and Pie Charts.
        *   Requires aggregate mathematical calculations for Shooting Percentages (FG%, 3P%) and total rebounds/assists.
    *   **Tab 2 - Player Data**:
        *   Displays the individual box score stats for every player in that specific game.
        *   Reuses the third-party `AdaptiveTableLayout` (the 2D scrollable spreadsheet).

## 2. Compose & Architectural Migration Strategy
We will replace the Fragment transactions with Jetpack Navigation Compose routing.

### UI Layer (Compose)
1.  **`GameHistoryListScreen`**:
    *   Use a `LazyColumn` to implement the history list.
    *   Implement Android's native `Intent.createChooser` to preserve the "Share" functionality.
2.  **`GameHistoryDetailScreen`**:
    *   Use `TabRow` and `HorizontalPager`.
    *   **Tab 1 (Visual Charts)**: Completely discard the legacy custom views. Use Compose native `Canvas` to draw percentage-based Progress Bars and comparative Bar Charts for a modern look.
    *   **Tab 2 (Spreadsheet)**: Directly reuse the 2D scrollable spreadsheet component we will build during the `GameBoxScore` migration.

### Presentation / Data Layer (MVVM)
1.  **`GameHistoryListViewModel`**:
    *   Fetches all `GameInfo` entities from the DAO where the game status is marked as "Finished".
2.  **`GameHistoryDetailViewModel`**:
    *   Receives the `gameId` as a navigation argument.
    *   **Heavy Lifting**: Because it needs to generate overall team comparisons (e.g., Team FG%), the ViewModel must launch a background Coroutine, fetch *all* `GameEvents` for this specific game from the DAO, and perform heavy math aggregations.
    *   Once calculated, it packages the results into `DetailUiState` and exposes it to the UI for charting.

## 3. Subagent Task Breakdown
*   **Step 1**: Configure Navigation Compose routing, enabling the List screen to pass the `gameId` argument to the Detail screen.
*   **Step 2**: Implement `GameHistoryListViewModel` and build the list UI, including the Share Intent logic.
*   **Step 3**: Implement `GameHistoryDetailViewModel` with the asynchronous data aggregation logic (calculating shooting percentages, total rebounds, etc.).
*   **Step 4**: Implement the Detail screen's `TabRow`, and use Compose Canvas/ProgressBars to build the Team Data comparative charts.
*   **Step 5**: Integrate the reusable Spreadsheet component from GameBoxScore to complete the Player Data tab.

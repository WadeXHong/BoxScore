# Feature Behavior Specification: Game History (`gamehistory`)

## 1. Feature Overview
The **Game History** module allows users to browse a chronological list of previously recorded games. Users can tap into any specific game to view a detailed breakdown, including both aggregate team statistics (e.g., charts, comparison tables) and individual player box scores.

## 2. Navigation Flow & State Machine
This module uses a Master-Detail flow hosted within `GameHistoryActivity`.

*   **Entry Point:** `GameHistoryActivity`
*   **Master View: History List (`HistoryMainFragment`)**
    *   *Action:* Displays a list of all finished games. 
    *   *Action:* Tapping a specific game item navigates forward to the detail view (`HistoryDetailFragment`).
    *   *Action:* Tapping the "More" (Share) text on an item likely triggers an export or share intent.
*   **Detail View: Game Analysis (`HistoryDetailFragment`)**
    *   Contains a `TabLayout` and a `NonSwipeableViewPager` to divide the detailed stats into two categories.
    *   **Tab 1: Team Data (`HistoryTeamDataFragment`)**
        *   Displays aggregate game stats (e.g., shooting percentages, total rebounds) comparing "Your Team" vs "Opponent".
    *   **Tab 2: Player Data (`HistoryPlayersDataFragment`)**
        *   Displays the full box score spreadsheet for the individual players in that specific game.

## 3. UI/UX & Call-to-Actions (CTAs)
*   **Activity Host (`activity_game_history.xml`)**
    *   **UI Elements:** A standard `Toolbar` with an orange background (`@color/colorOrange`) and white text. It hosts a `FrameLayout` for fragment swapping.
*   **Game List (`fragment_history_main.xml`)**
    *   **UI Elements:** A header row denoting columns (Date/Title, Your Team, Score, Opponent) and a `RecyclerView`.
    *   **List Item (`item_game_history.xml`):** Shows the game date, teams involved, final score, and a "More" (`更多`) CTA text on the far right.
*   **Detail Container (`fragment_history_detail.xml`)**
    *   **UI Elements:** A `TabLayout` with a dark secondary indicator (`@color/secondaryDarkColor`) over a `NonSwipeableViewPager`.
*   **Team Data Tab (`fragment_history_team_data.xml`)**
    *   **UI Elements:** A `RecyclerView` composed of specialized layout items (`item_history_teamdata_chart.xml`, `item_history_teamdata_table.xml`) to render visual data comparisons.
*   **Player Data Tab (`fragment_history_players_data.xml`)**
    *   **UI Elements:** Utilizes the third-party `AdaptiveTableLayout` (identical to the live box score feature) to render the 2D scrollable box score grid.

## 4. Data Dependencies (Current State)
*   **Presenters:** `GameHistoryPresenter` orchestrates the fragment swaps. `HistoryMainPresenter`, `HistoryDetailPresenter`, `HistoryTeamDataPresenter`, and `HistoryPlayersDataPresenter` handle data fetching.
*   **Local Database:** Requires heavy read access to `GameInfoDbHelper` (for the master list) and `GameDataDbHelper` (to aggregate the team and player stats for the detail view).

## 5. Migration Notes for Compose / MVVM
*   **Navigation:** The Fragment transaction from Master -> Detail should be replaced by Jetpack Navigation Compose (e.g., `NavHost` passing the `gameId` as an argument to the Detail screen).
*   **Data Aggregation:** Calculating team percentages and charting data is currently likely done on the main thread in Presenters/Adapters. In MVVM, this heavy lifting should be moved to a `Coroutine` inside the `GameHistoryViewModel`, exposing the fully calculated chart data as a `StateFlow` to the Compose UI.
*   **Charting:** Legacy custom views or `RecyclerView` chart items must be replaced. We should evaluate using a modern Compose charting library (e.g., Vico) or drawing custom `Canvas` graphs for the Team Data tab.

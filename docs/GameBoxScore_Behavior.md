# Feature Behavior Specification: Game Box Score (`gameboxscore`)

## 1. Feature Overview
The **Game Box Score** module is the core scorekeeping interface of the app. It is used during a live game to track points, fouls, rebounds, and other stats for the user's team, while providing quick adjustments for the opposing team's score and fouls. 

## 2. Navigation Flow & State Machine
The screen is a Single-Activity (`GameBoxScoreActivity`) split into two main sections: a persistent top scoreboard and a bottom `ViewPager` containing three distinct action fragments.

*   **Entry Point:** `GameBoxScoreActivity` (Receives `GameInfo` from the Start Game wizard).
*   **Persistent Scoreboard (Top Half)**
    *   Displays current Quarter, Your Team Score, Opponent Score, Your Team Fouls, Opponent Fouls.
    *   *Action:* Tap `[ + ]` or `[ - ]` next to the opponent's score/fouls to quickly adjust them.
    *   *Action:* Tap "Save" icon -> Saves current game state.
    *   *Action:* Tap "DataStatistic" icon -> Opens `DataStatisticDialog`.
*   **Tab 1: Data Record (`DataRecordFragment`)**
    *   The primary interface for recording actions.
    *   *Action:* Tap a stat button (e.g., "+2 Point", "Assist") -> Opens `PlayerSelectDialog` to attribute the stat to a specific player currently on the court.
*   **Tab 2: Change Player (`ChangePlayerFragment`)**
    *   Manages substitutions.
    *   *Action:* Tap a player -> Opens `ChangePlayerDialog` to swap them with a benched player.
*   **Tab 3: Undo History (`UndoHistoryFragment`)**
    *   A chronological log of recorded actions.
    *   *Action:* Tap an action -> Opens an Undo dialog to revert the mistake and deduct the points/stats.
*   **Data Statistic Dialog (`DataStatisticDialog`)**
    *   A full-screen modal showing a comprehensive spreadsheet of all players' stats.
    *   *Action:* Tap top half ("Go Back") to dismiss.

## 3. UI/UX & Call-to-Actions (CTAs)
*   **Activity Scoreboard (`activity_game_box_score.xml`)**
    *   **UI Elements:** Massive red text for scores (`60sp`), orange text for fouls and quarter (`35sp`, `45sp`). Dark background.
    *   **Opponent Adjustments:** Dedicated `+` and `-` icons directly next to the Opponent Score and Opponent Foul numbers for immediate access.
    *   **Top Bar Icons:** Small icons for Save (Floppy disk), Settings (Gear), and a custom table icon for Data Statistic.
*   **Data Record Form (`fragment_data_record.xml`)**
    *   **UI Elements:** A 2-column grid of large buttons.
    *   **Categories:** The buttons are split visually into "Offense" and "Defense" headers.
    *   **CTAs:** Buttons for: 2 Point Shot, 3 Point Shot, Free Throw, Offensive Rebound, Defensive Rebound, Assist, Steal, Block, Foul, Turnover.
*   **Undo History (`fragment_undo_history.xml`)**
    *   **UI Elements:** A header row (Quarter, Number, Name, Type) and a `RecyclerView` below.
*   **Data Statistic Spreadsheet (`dialogfragment_game_statistic.xml`)**
    *   **UI Elements:** Utilizes `AdaptiveTableLayout` to render a 2D scrollable spreadsheet of box score stats.

## 4. Business Logic (Presenter Layer)
*   **State Management (`GameBoxScorePresenter`):** Maintains the current state of the game using a `SparseIntArray` (`mTeamData`) for immediate UI updates and `SharedPreferenceHelper` for fast, temporary persistence (to survive process death or accidental closure).
*   **Undo/Redo Stack:** Maintains a `LinkedList<Undo>` (`mUndoList`). Every stat change records an `Undo` object containing the player, the action type, and the quarter.
*   **Database Writes:** Every time a stat is recorded (e.g., via `plusData` in `GameDataDbHelper`), the change is immediately written to the local SQLite database. 
*   **Multi-touch Gestures:** The presenter actively listens to multi-finger scroll events (`scrollUp`, `scrollDown`, `scrollLeft`, `scrollRight`) with 2 or 3 fingers, translating them into specific basketball stat actions defined in user settings.

## 5. Data Dependencies (Current State)
*   **Presenters:** `GameBoxScorePresenter` handles the massive overarching state. Sub-presenters (`DataRecordPresenter`, `UndoHistoryPresenter`, etc.) handle tab-specific logic.
*   **Local Database:** Heavy reliance on `GameDataDbHelper` to insert individual stats, and `GameInfoDbHelper` to update the rolling team score/fouls.
*   **Data Passing:** A complex web of callbacks and event buses to ensure that when a stat is recorded in `DataRecordFragment`, the `UndoHistoryFragment` refreshes, and the `Activity` updates the scoreboard.

## 5. Migration Notes for Compose / MVVM
*   **Layout Parity:** The top scoreboard relies heavily on absolute sizing (`60sp`, `45sp`) and `ConstraintLayout` bias to position the scores. Compose `ConstraintLayout` or weighted `Row`/`Column` modifiers must carefully match these proportions.
*   **State Management (Crucial):** This screen suffers from fragmented state in the legacy MVP pattern. In Compose, a single `GameBoxScoreViewModel` should expose a master `GameUiState` data class (containing score, fouls, court roster, and action history). All 3 tabs will simply observe and trigger intents on this single ViewModel, completely eliminating the need for complex inter-fragment communication.
*   **3rd Party Libraries:** The `AdaptiveTableLayout` in the Data Statistic dialog might need to be replaced by a Compose Lazy grid (e.g., synchronizing a `LazyColumn` of `LazyRow`s) if a direct Compose library isn't available.

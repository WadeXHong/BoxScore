# Migration Plan: Game Box Score

## 1. Business Logic & Existing Behavior Analysis
This is the core, most complex live-scoring interface in the app. The legacy architecture relies on multiple Presenters communicating via callback hell, a custom view for multi-finger swipes, and a complex spreadsheet dialog.

### Interface Layout & Interaction Logic
*   **Top Half: Persistent Scoreboard**
    *   Displays current Quarter (45sp), Your Team Score/Fouls (Giant fonts), and Opponent Score/Fouls.
    *   **Quick Actions**: The opponent's score and fouls have dedicated `[+]` and `[-]` buttons directly next to them for immediate adjustments (bypassing the menu).
    *   **System Actions**: Top bar contains a Save icon (Floppy disk) and a DataStatistic icon.
*   **Bottom Half: Action Panel (3 Tabs in ViewPager)**
    *   **Tab 1 - Data Record**: Buttons split into "Offense" and "Defense" zones (+2, +3, FT, OReb, DReb, Ast, Stl, Blk, Foul, TO). Tapping a stat opens `PlayerSelectDialog` to attribute the action to a player currently on the court.
    *   **Tab 2 - Change Player**: Manages substitutions. Tapping a player opens `ChangePlayerDialog` to swap bench players with court players.
    *   **Tab 3 - Undo History**: A chronological log of actions. Tapping a record opens an Undo dialog; confirming it deducts the stats and points from the scoreboard.
*   **Data Statistic Spreadsheet Dialog**
    *   A full-screen modal. The legacy app used a third-party `AdaptiveTableLayout` to render a **2D Scrollable Spreadsheet** showing all players' detailed stats.
*   **Multi-finger Gestures (Shortcuts)**
    *   The legacy app intercepts touches via a custom `BSLinearLayout`.
    *   Detects 2-finger or 3-finger swipes (Up/Down/Left/Right).
    *   Maps the swipe direction to a basketball action via `SharedPreferences` (e.g., 2-fingers Up = +2 Points) and directly triggers `popPlayerSelectProcess`, skipping the button tap.

## 2. Compose & Architectural Migration Strategy
This module is highly prone to state fragmentation in the legacy MVP pattern. We will rebuild it using a Single Source of Truth (SSOT) via `GameBoxScoreViewModel` and an Event Sourcing approach.

### UI Layer (Compose)
1.  **`GameBoxScoreScreen`**: Retains the giant-font scoreboard at the top, and uses a `HorizontalPager` or `TabRow` for the bottom half.
2.  **Custom Multi-Finger Swipe Modifier (`Modifier.multiFingerSwipe`)**:
    *   Eliminate `BSLinearLayout`.
    *   Use Compose's `pointerInput(Unit)` and `awaitEachGesture` to detect `changes.size` (number of fingers) and `positionChange()` (swipe direction).
    *   Pass the recognized gesture as an Intent to the ViewModel.
3.  **Dialog Rebuilding**:
    *   Implement `PlayerSelectDialog` and `ChangePlayerDialog`.
    *   **The Spreadsheet Challenge**: For `DataStatisticDialog`, we will implement a synchronized `LazyColumn` and `LazyRow` to achieve a frozen-header, 2D scrollable data table natively in Compose.

### Presentation / Data Layer (MVVM)
1.  **`GameBoxScoreViewModel`**:
    *   Maintains `GameUiState` (current score, players on court, benched players, action history).
    *   **Event Sourcing Concept**: Scores and fouls are not modified as plain integers. Instead, every action generates a `GameEvent` entity (inserted into Room DB `GameEventDao`). The total score is re-calculated dynamically from the database. This guarantees the absolute correctness of the "Undo" logic.

## 3. Subagent Task Breakdown
*   **Step 1 (Data Engine)**: Implement `GameBoxScoreViewModel` and the Event-based Room Schema. Establish the core logic for scoring, substituting, and undoing events.
*   **Step 2 (UI Core)**: Build the Top Scoreboard UI and implement the custom `Modifier.multiFingerSwipe` gesture detector in Compose.
*   **Step 3 (UI Tabs)**: Build the bottom 3 action tabs (Grid buttons, Player swap list, Undo chronological log) and their associated popup Dialogs.
*   **Step 4 (Complex UI)**: Tackle the full-screen `DataStatisticDialog`, implementing the 2D scrollable spreadsheet in Compose.

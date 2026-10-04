# Game Box Score: Detailed Migration Plan

This document provides a comprehensive breakdown of the `GameBoxScore` module to facilitate parallel migration to Jetpack Compose and MVVM. This plan incorporates precise UI metrics (colors, padding, sizing) extracted from the legacy XML files to ensure perfect layout parity.

## Phase 1: Core State Management & Data Layer (Agent 1) [✅ COMPLETED]
**Objective**: Establish the single source of truth for the entire Game Box Score module.
*   **Component**: `GameBoxScoreViewModel`
*   **Details**:
    *   Define a robust `GameUiState` data class containing: `yourScore`, `opponentScore`, `quarter`, `yourFouls`, `opponentFouls`, `onCourtPlayers`, `benchPlayers`, and `actionHistory`.
    *   Implement Intent handlers: `onOpponentScoreChanged()`, `onOpponentFoulChanged()`, `onStatRecorded()`, `onPlayerSubstituted()`, `onUndoAction()`.
    *   Integrate Room Database DAOs (`GameDao`, `GameEventDao`, `PlayerDao`) using Kotlin Coroutines/Flow to ensure UI state reflects database changes.
    *   Migrate the legacy `mUndoList` logic to leverage the newly created `GameEventEntity` for persistent undo tracking.

## Phase 2: Top Scoreboard UI (Agent 2) [✅ COMPLETED]
**Objective**: Recreate the persistent top half of the screen (`activity_game_box_score.xml`).
*   **Component**: `ScoreboardSection.kt`
*   **UI Details (Strict Parity)**:
    *   **Background**: Use `colorDarkBackground`.
    *   **Scores**: `60sp`, `colorRed`, bold. Positioned via constraints with horizontal bias `0.2` (Your Team) and `0.8` (Opponent).
    *   **Fouls**: `35sp`, `colorOrange`. Horizontal bias `0.10` (Your Team) and `0.9` (Opponent). The text "團犯" (`colorButtonInMain`, `15sp`) is placed directly below the foul numbers.
    *   **Quarter**: `45sp`, `colorOrange`. Positioned in the center (bias `0.6` vertically).
    *   **Opponent Adjustments**: `30dp` x `30dp` icons (`ic_plus_box_white_24dp` and `ic_minus_check_box_white_24dp`) placed immediately to the right of the opponent score and fouls.
    *   **Top Bar Icons**: `ic_save` (top-left, 30dp wide, 23dp start margin), `ic_setting_white` (top-right, 35dp wide, 23dp end margin), `ripple_table` (bottom-center, 40dp x 40dp).
    *   **Makeup Button (補登)**: `15sp`, `colorButtonInMain`, 50dp wide, with background `shape_edittext_gamename`.

## Phase 3: Data Record Tab & Player Select (Agent 3) [✅ COMPLETED]
**Objective**: Implement the primary stat recording interface (`fragment_data_record.xml`).
*   **Component**: `DataRecordTab.kt` & `PlayerSelectDialog.kt`
*   **UI Details (Strict Parity)**:
    *   **Root container**: `10dp` padding all around, transparent background.
    *   **Headers**: "Offense" and "Defense", `20sp`, `colorButtonInMain`.
    *   **Stat Buttons**: Arranged in a 2-column grid. Each button must have `8dp` margin, `20sp` text size, and use the ripple effect (`ripple_btn_datarecord`).
    *   **Dialog**: A modal bottom sheet or dialog (`PlayerSelectDialog.kt`) that appears when a stat button is tapped, listing the 5 players currently on the court.

## Phase 4: Change Player Tab & Dialog (Agent 4)
**Objective**: Implement substitution logic and UI (`fragment_change_player.xml` & `item_playeroncourt_players.xml`).
*   **Component**: `ChangePlayerTab.kt` & `ChangePlayerDialog.kt`
*   **UI Details (Strict Parity)**:
    *   **Row Item**: Fixed height `55dp`, background `colorButtonInMain`, padding `10dp`.
    *   **Number Text**: `20sp`, `colorTextInMainButton`, max length 2. Needs a `20dp` end margin.
    *   **Name Text**: `20sp`, `colorTextInMainButton`. Needs a `70dp` start margin.
    *   **Logic**: When a court player is tapped, show a list of eligible bench players to swap with. Ensure validation logic remains intact (exactly 5 players on the court).

## Phase 5: Undo History Tab (Agent 5)
**Objective**: Implement the chronological log of actions (`fragment_undo_history.xml` & `item_undohistory.xml`).
*   **Component**: `UndoHistoryTab.kt`
*   **UI Details (Strict Parity)**:
    *   **Header Row**: Height `40dp`. Contains 4 columns (Quarter, Number, Name, Type). Text is `20sp`, `colorButtonInMain`.
    *   **Row Weights**: The horizontal weights for the 4 columns MUST be exactly `0.8`, `0.8`, `1.5`, and `1.7`.
    *   **List Item**: Fixed height `55dp`, background `ripple_layout_darkgray`. Same weights and text sizes as the header.
    *   **Logic**: Tapping an item triggers an undo confirmation dialog and dispatches the event to the ViewModel.

## Phase 6: Data Statistic Spreadsheet (Agent 6)
**Objective**: Recreate the complex 2D scrollable stat table (`dialogfragment_game_statistic.xml`).
*   **Component**: `DataStatisticDialog.kt`
*   **UI Details (Strict Parity)**:
    *   **Layout Structure**: The dialog is split vertically with a weight ratio of `1` (Top) to `3` (Bottom). Background is `colorDialogBackground`.
    *   **Top Section (Dismiss Area)**: A giant "Go Back" text, sized `60sp`, `colorButtonInMain`, centered. Tapping this area closes the dialog.
    *   **Bottom Section (Table)**: Replace the legacy `AdaptiveTableLayout`. Implement a 2D scrolling grid in Compose using a `LazyColumn` of `LazyRow`s with synchronized scroll states.
    *   **Table Styling**: Background `colorOrange`, cell margins `1dp`, with fixed sticky headers for both the top row and the first column.

## Phase 7: Activity Integration & Gestures (Agent 7) [🚧 IN PROGRESS]
**Objective**: Final assembly and custom gesture handling (`GameBoxScoreActivity.kt`).
*   **Component**: `GameBoxScoreActivity.kt` & `GameBoxScoreScreen.kt`
*   **Details**:
    *   Create the true `GameBoxScoreActivity.kt` and set `setContent { GameBoxScoreScreen(...) }`.
    *   Assemble the Top Scoreboard and the bottom `HorizontalPager` (Tabs 1-3) into a single cohesive screen. The ViewPager tab indicator uses `secondaryDarkColor`.
    *   **Multi-touch Gestures**: Implement the custom `Modifier.multiFingerSwipe` to detect 2 and 3 finger directional swipes (`awaitEachGesture`), mapping them to specific stat recording actions based on user settings.

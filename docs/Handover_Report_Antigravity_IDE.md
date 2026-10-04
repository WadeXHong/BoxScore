# BoxScore Project Handover Report (For Antigravity IDE)

## 📌 Project Overview
The objective is to migrate the legacy **BoxScore** Android application from a Java/XML MVP architecture to a modern **Kotlin + Jetpack Compose + MVVM** architecture.

## 🚨 CRITICAL STRICT RULES (Must Follow)
1. **Never Delete Legacy Code**: Old Java and XML files must be preserved for side-by-side comparison. They have been moved/renamed (e.g., `LegacyGameBoxScoreActivity.java` in the `legacy/` package) but must remain in the codebase.
2. **Pure Compose Layouts**: Avoid using `androidx.constraintlayout:constraintlayout-compose`. Use pure Compose modifiers (like `Row`, `Column`, `Box`, and `weight`) to achieve complex biases and UI layouts.
3. **Material 3**: Strictly use `androidx.compose.material3.*` components and properties (e.g., `containerColor` instead of `backgroundColor`).

## 📊 Current Migration Status: GameBoxScore Module
We are following the 7-phase plan outlined in `docs/GameBoxScore_Migration_Plan.md`. 
**Current State: The app successfully compiles and launches the new Compose UI!**

### ✅ Completed Work
*   **Phase 1 (Core State & Data)**: 
    *   `GameBoxScoreViewModel.kt` and `GameUiState` are fully implemented.
    *   Room DB DAOs are integrated.
*   **Phase 2 (Top Scoreboard UI)**: 
    *   `ScoreboardSection.kt` implemented using pure Compose weights.
    *   Upgraded to Material 3. Includes a `@Preview` function.
*   **Phase 3 (Data Record UI)**: 
    *   `DataRecordTab.kt` (Button grid) and `PlayerSelectDialog.kt` implemented in Material 3. Includes `@Preview`.
*   **Phase 7 (Partial Integration)**: 
    *   Legacy Activity renamed to `LegacyGameBoxScoreActivity`.
    *   New `GameBoxScoreActivity.kt` created.
    *   `GameBoxScoreScreen.kt` created, successfully stacking `ScoreboardSection` and `DataRecordTab`. 

### 🚧 Outstanding Work (Next Steps for IDE)
*   **Phase 4 (Change Player)**: Implement `ChangePlayerTab.kt` and substitution UI.
*   **Phase 5 (Undo History)**: Implement `UndoHistoryTab.kt` showing a chronological log of events.
*   **Phase 6 (Data Statistic Spreadsheet)**: Build `DataStatisticDialog.kt`, a complex 2D scrollable grid replacing the legacy `AdaptiveTableLayout`.
*   **Phase 7 (Finalization)**: 
    *   Combine all tabs (Data Record, Change Player, Undo History) into a `HorizontalPager` inside `GameBoxScoreScreen`.
    *   Implement multi-finger swipe gestures (`Modifier.pointerInput`) on the screen to handle quick stat recording.

## 🐛 Context / Quirks to Watch Out For
1. **Type Mismatches**: When recording a stat, `DataRecordTab` emits a string (e.g., "2PM"). This must be mapped to the integer constants in `Constants.RecordDataType` (e.g., `TWO_POINT_SHOT_MADE`) before calling `ViewModel.onStatRecorded`. This mapping is currently handled in `GameBoxScoreScreen.kt`.
2. **Entity IDs**: `TeamPlayerEntity` has a database `id: Long`, but the domain logic uses `playerId: String`. Make sure to pass `player.playerId`, not `player.id`.
3. **Compose Drawable Crash**: `painterResource` does not support stateful XML drawables like `<ripple>` or `<selector>`. We had a runtime crash loading `ripple_table.xml`. Always load the raw vector drawable (e.g., `ic_table`) and rely on Compose's built-in ripple effects (like inside `IconButton`).

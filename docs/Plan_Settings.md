# Migration Plan: Settings

## 1. Business Logic & Existing Behavior Analysis
`SettingActivity` provides the app's preference configurations. The current implementation relies on a `RecyclerView` to build heterogeneous settings items (`item_setting_header`, `item_setting_others`) and stores all data in the legacy Android `SharedPreferences`.

### Critical Missing Behavior: Gesture Configuration
*   **Current State**: In the legacy XML, the multi-finger swipe configurations (`item_setting_gesture_parent` and `child`) have been commented out or hidden.
*   **Business Logic Implication**: As analyzed in the `GameBoxScore` module, the app supports 2-finger and 3-finger swipe shortcuts (Up/Down/Left/Right). If users cannot map these directional swipes to specific basketball actions (e.g., 2-Fingers Left = Assist) in the Settings page, this powerful feature becomes useless.
*   **Goal**: We must **revive and reimplement** these gesture mapping configurations with dropdown menus in the new UI.

## 2. Compose & Architectural Migration Strategy
We will abandon `RecyclerView` in favor of declarative Compose UI, and replace the obsolete `SharedPreferences` with the asynchronous, type-safe Jetpack `Preferences DataStore`.

### UI Layer (Compose)
1.  **`SettingsScreen`**:
    *   Layout using `LazyColumn`.
    *   **Generic Settings Area**: Use standard Compose `Switch`, `Text`, and `Icon` for standard toggles and clickable items.
    *   **Multi-Finger Gestures Area**:
        *   Provide a dedicated section allowing users to map 2-finger and 3-finger swipes (8 directions total).
        *   Use Compose's `ExposedDropdownMenuBox` to provide a dropdown list of all available basketball actions (+2 points, +3 points, Rebounds, Assists, etc.) for each swipe direction.

### Presentation / Data Layer (MVVM)
1.  **Data Migration**:
    *   Establish a `PreferencesDataStore`.
    *   **Crucial Logic**: During DataStore initialization, write a one-time migration script that reads the legacy values from `SharedPreferenceHelper` and transfers them seamlessly to DataStore. This ensures existing users do not lose their settings upon updating.
2.  **`SettingsViewModel`**:
    *   Collects the DataStore Flow and exposes it as a type-safe `SettingsUiState`.
    *   Provides explicit Intents (e.g., `updateGestureMapping(direction, actionId)`) allowing the UI to easily write changes back to the database.

## 3. Subagent Task Breakdown
*   **Step 1 (Data Layer)**: Setup Jetpack DataStore and implement the seamless Migration Block from the legacy `SharedPreferences`.
*   **Step 2 (Architecture)**: Implement `SettingsViewModel` to handle reading and writing preference Flows.
*   **Step 3 (UI - Generic)**: Build the generic `LazyColumn` settings list (Headers, Toggles, Clickable rows) using Compose.
*   **Step 4 (UI - Gestures)**: Build the Gesture Shortcut mapping UI, implementing the `ExposedDropdownMenuBox` so users can bind swipe directions to game actions.

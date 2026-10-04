# Feature Behavior Specification: Settings (`setting`)

## 1. Feature Overview
The **Settings** module provides a configuration menu for the application. Based on the legacy code, the UI has been largely simplified to a single list displaying various configuration options.

## 2. Navigation Flow & State Machine
*   **Entry Point:** `SettingActivity`
*   **Action Flow:** 
    *   The user interacts with a `RecyclerView` list of settings.
    *   Tapping specific items likely triggers dialogs, toggles `SharedPreferences`, or initiates actions (e.g., logging out, syncing data).

## 3. UI/UX & Call-to-Actions (CTAs)
*   **Activity Host (`activity_setting.xml`)**
    *   **UI Elements:** A large portion of the legacy XML layout (dealing with brightness seekbars and multi-finger custom gestures) is completely commented out.
    *   **Active UI:** The only active component is a `RecyclerView` (`activity_setting_recyclerview`) sitting on a dark background (`@color/colorDarkBackground`).
*   **List Items**
    *   The `RecyclerView` utilizes several item layouts to build a heterogeneous list (similar to a standard Android Preference Screen):
        *   `item_setting_header.xml`: Category titles.
        *   `item_setting_others.xml`: Standard clickable setting items.
        *   *(Legacy)* `item_setting_gesture_parent` / `child`: Remnants of an older gesture configuration system.

## 4. Data Dependencies (Current State)
*   **Presenters:** `SettingPresenter` handles the population of the `RecyclerView` list.
*   **Data Storage:** Relies primarily on Android's `SharedPreferences` (via a helper like `SharedPreferenceHelper`) to persist user configuration toggles.

## 5. Migration Notes for Compose / MVVM
*   **Layout:** The `RecyclerView` can be easily replaced with a `LazyColumn` in Compose.
*   **Data Layer:** `SharedPreferences` should be formally migrated to **Jetpack DataStore (Preferences DataStore)** during Phase 3. 
*   **State Management:** The `SettingViewModel` should observe the DataStore `Flow` and expose the current settings as a `StateFlow` to the Compose UI, allowing for instant reactive updates when a toggle is changed.

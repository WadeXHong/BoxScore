# BoxScore Refactoring Plan

## 1. Current Project Status & Architecture Analysis

Based on the review of the current `BoxScore` repository, here is the assessment of the existing architecture and structure:

*   **Language:** 100% Java.
*   **Build System:** Gradle with Groovy DSL. Very outdated versions (Android Gradle Plugin `3.0.1`, SDK `27`).
*   **UI Toolkit:** XML Layouts, legacy Android Support Library (`android.support.*`), Activities, and Fragments.
*   **Architecture:** MVP (Model-View-Presenter). Codebase utilizes `BasePresenter`, `BaseView`, and Contract interfaces (e.g., `BoxScoreContract`, `BoxScorePresenter`).
*   **Data Layer:**
    *   *Local DB:* Raw `SQLiteOpenHelper` is used manually across classes like `GameDataDbHelper.java`, `TeamDbHelper.java`.
    *   *Remote:* Firebase integration (Auth, Database, Storage, Crash).
    *   *Preferences:* `SharedPreferenceHelper.java` utilizing raw SharedPreferences.
*   **Concurrency:** Heavy reliance on deprecated `AsyncTask` for background operations (e.g., `CreateExcelTask.java`, `OnCreateDataBaseTask.java`).
*   **Packaging:** Mostly feature-based (`login`, `teammanage`, `gamehistory`) combined with layer-based (`modelhelper`, `adapter`).

---

## 2. Target Modern Architecture (The "Extremely Goal")

To align with the latest Android ecosystem best practices, the project will be refactored towards the following stack:

*   **Language:** 100% **Kotlin**.
*   **Build System:** **AGP 8.x+**, **Kotlin DSL (`build.gradle.kts`)**, and **Version Catalogs (`libs.versions.toml`)**.
*   **UI Toolkit:** **Jetpack Compose**. Single-Activity Architecture.
*   **Architecture:** **Clean Architecture** + **MVVM** (Model-View-ViewModel) + **UDF** (Unidirectional Data Flow).
*   **Data Layer:** **Room Database** (for SQLite), **Jetpack DataStore** (for Preferences), and Kotlin-friendly Firebase SDKs.
*   **Concurrency:** Kotlin **Coroutines** and **Flow** (replacing AsyncTask and Callbacks).
*   **Navigation:** **Compose Navigation**.
*   **Dependency Injection:** **Hilt** (Dagger).

---

## 3. Global Foundation Setup

Refactoring a legacy app requires a disciplined approach. Instead of migrating the entire application horizontally (e.g., all databases first, then all UI), we will adopt a **Vertical Slice Migration Strategy (Feature by Feature)**. 

First, we will modernize the global build system and core infrastructure. 

### Phase 1: Global Build System Modernization
*Applies to the entire project before touching feature code.*
1.  **Update Gradle & AGP:** Incrementally upgrade Gradle Wrapper and Android Gradle Plugin to the latest stable versions.
2.  **Migrate to AndroidX:** Use the automated tool (or manual refactoring) to migrate `android.support.*` dependencies to `androidx.*`.
3.  **Update SDK Versions:** Bump `compileSdkVersion` and `targetSdkVersion` to `34` (or latest).
4.  **Introduce Kotlin:** Apply the `kotlin-android` plugin and add the Kotlin standard library. 
5.  **Migrate Build Scripts:** Convert `build.gradle` from Groovy to Kotlin DSL (`build.gradle.kts`) and centralize dependencies using Version Catalogs.

### Phase 2: Global Core Infrastructure Setup
*Setup the foundational libraries required for the new architecture.*
1.  **Integrate Hilt:** Setup Hilt in the `Application` class and configure base modules.
2.  **Add Modern Jetpack Dependencies:** Add dependencies for Room, Lifecycle (ViewModel), Coroutines, and Compose.
3.  **Setup Room Database instance:** Create the global `RoomDatabase` instance (tables/entities will be added iteratively per feature).
4.  **Setup Custom Compose Theme:** Define a custom Compose Theme that perfectly mimics the legacy XML colors, typography, and shapes. *Avoid Material 3 components and defaults to ensure strict visual parity.*

---

## 4. Vertical Slice Refactoring (Feature by Feature)

After establishing the foundation in Phases 1 and 2, we will take **one functional screen at a time** (e.g., Login Page, Game History Page, Box Score Page) and migrate it entirely from the Data layer up to the Jetpack Compose UI. 

*For EVERY feature/screen chosen, repeat Phase 3 through Phase 7 sequentially before moving to the next feature.*

### Feature Behavior Specifications
Before migrating any feature, we must document its exact legacy behavior to ensure 1:1 parity in the new architecture. You can reference the detailed behavioral specifications here:
*   [Team Management Flow](docs/TeamManage_Behavior.md)
*   [Start Game Wizard](docs/StartGame_Behavior.md)
*   [Live Game Box Score](docs/GameBoxScore_Behavior.md)
*   [Game History](docs/GameHistory_Behavior.md)
*   [Dashboard & Authentication](docs/DashboardAndAuth_Behavior.md)
*   [Settings](docs/Settings_Behavior.md)

### Phase 3: Custom Layouts & Touch Gestures
The legacy app heavily utilizes custom `ViewGroups` to handle complex, multi-touch gestures and custom scrolling logic. These must be thoughtfully translated to Compose:
1.  **`BSLinearLayout` (Multi-finger Swipe):** Legacy tracks 2-finger and 3-finger swipes via `onTouchEvent` to trigger stat actions. 
    *   *Compose Strategy:* Use Compose's `pointerInput` modifier with `awaitPointerEventScope` to track multi-touch events (`event.changes.size`) and calculate swipe directions, replacing the custom `LinearLayout`.
2.  **`BSViewPager` & `NonSwipeableViewPager`:** Legacy intercepts touches to conditionally block swiping.
    *   *Compose Strategy:* Use `HorizontalPager` with a custom `userScrollEnabled` state driven by the `ViewModel` (e.g., blocking swipe until validation passes).
3.  **`BSConstraintLayout`:** Legacy consumes all touches (returns `true` in `onTouchEvent`).
    *   *Compose Strategy:* Apply the `Modifier.pointerInput(Unit) { detectTapGestures { } }` to consume pointer events without rippling, acting as a touch barrier.

### Phase 4: Data Layer Refactoring (For this feature)
1.  **Convert Models:** Translate Java POJOs relevant to this feature into Kotlin `data class`.
2.  **Migrate DB/Preferences:** Create Room `@Entity` and `@Dao` specifically for this feature. Migrate feature-specific `SharedPreferenceHelper` logic to Jetpack DataStore.
3.  **Create Repositories:** Introduce a Repository to abstract data sources (Room + Firebase) and return Coroutine `Flow`s.

### Phase 5: Domain Layer Introduction (For this feature)
1.  **Create Use Cases:** Extract business logic out of this feature's Presenters and `AsyncTask`s into standalone Kotlin Use Cases.
2.  **Coroutines Migration:** Ensure all background tasks for this feature utilize Coroutines, eliminating `AsyncTask`.

### Phase 6: Presentation Layer Refactoring (For this feature)
1.  **Introduce ViewModels:** Create Jetpack `ViewModel` classes to replace the MVP `Presenter` for this screen.
2.  **Implement State Management:** Expose UI state via `StateFlow` and handle UI events using a unidirectional data flow pattern.
3.  **Deprecate MVP:** Safely remove or deprecate this feature's `*Contract` interfaces.

### Phase 7: UI Layer Refactoring (For this feature)
**CRITICAL REQUIREMENT:** The new Composable UI must match the previous XML design as closely as possible. **DO NOT** use Material 3 at the beginning. Rely on basic Compose foundations and the custom theme to replicate the exact look and feel of the original app.
1.  **1:1 Component Migration:** Rebuild low-level UI components needed for this screen in Compose so they look absolutely identical to their legacy XML counterparts.
2.  **Screen Migration:** Refactor the Activity/Fragment into a Compose screen. Use the `ComposeView` interop if mixing Compose with legacy XML during the transition.
3.  **Navigation Migration:** Replace `Intent` navigation for this screen with `Compose Navigation`.

### Phase 8: Cleanup & Optimization (For this feature)
1.  **Deprecate, Do Not Delete:** Do **NOT** remove the legacy XML layouts, Java files, `AsyncTask`s, or SQLiteHelpers related to this feature. Instead, mark them as `@Deprecated` (or move them to a dedicated `legacy` package). We must keep them as a backup and reference to ensure the new implementation works identically to the original and to aid in debugging if regressions occur.
2.  **Testing:** Add Unit tests for the new Use Cases and ViewModels using `JUnit5`, `MockK`, and Coroutines Test libraries.
3.  **Performance Profiling:** Analyze the feature with Android Studio Profiler and Perfetto to ensure smooth performance and low memory usage.

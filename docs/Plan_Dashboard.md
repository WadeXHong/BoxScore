# Migration Plan: Dashboard & Authentication

## 1. Business Logic & Existing Behavior Analysis
Currently, `BoxScoreActivity` takes on an highly unconventional dual role: it serves as **both the Dashboard (Main Menu) and the Authentication entry point**.
*   **Routing Origin**: The app automatically routes here from `LogoTransitionActivity` (Splash Screen).
*   **UI Layout Characteristics**: It is a giant grid interface split into 4 rows, featuring a branded background (`@drawable/background_main`) and a massive Logo (`@drawable/logo_main`).
    *   Row 1: [Start Game Button] | [Email Input]
    *   Row 2: [Team Manage Button] | [Password Input]
    *   Row 3: [Game History Button] | [Login Button]
    *   Row 4: [Setting Button] | [Sign Up Button]
*   **Design Elements**: All inputs and buttons share the exact same rounded rectangle shape (`@drawable/shape_btn_main_ori` and `ripple_btn_main`).
*   **Logic Coupling**: The legacy `activity_login.xml` is completely empty (a "Hello World" placeholder). This proves that all login, signup, and menu navigation logic is hardcoded inside `BoxScorePresenter` and this single Activity.

## 2. Compose & Architectural Migration Strategy
To adhere to modern Android development standards (Separation of Concerns), we must split this monolithic UI into **two distinct Compose screens**, managed by `NavHost` based on the user's authentication state.

### UI Layer (Compose)
1.  **`LoginScreen` (Auth UI)**:
    *   Contains the Logo, Email and Password `OutlinedTextField`s, and the "Login" / "Sign Up" CTAs.
    *   Use Compose's `RoundedCornerShape` to perfectly replicate the legacy rounded button and input designs.
2.  **`DashboardScreen` (Main Menu UI)**:
    *   Contains the Logo and the four main navigation CTAs (Start Game, Team Manage, Game History, Settings).
    *   If the user wishes to maintain the legacy "side-by-side" grid look, we can use a `Row` with `weight(1f)` `Column`s, but keep it completely decoupled from the Auth UI.

### Presentation Layer (MVVM)
1.  **`AuthViewModel`**:
    *   Handles Email/Password validation logic.
    *   **Core Logic**: Encapsulate Firebase Authentication's asynchronous callbacks into Kotlin Coroutines (`suspend` functions).
    *   Exposes `AuthUiState` (Loading, Success, Error messages).
2.  **`DashboardViewModel`**:
    *   Observes the current Firebase `currentUser` state (UserSession).
    *   Determines whether the `NavHost`'s start destination should be `login` or `dashboard`.

## 3. Subagent Task Breakdown
*   **Step 1**: Setup Jetpack Navigation Compose in the module, creating `LoginRoute` and `DashboardRoute`.
*   **Step 2**: Implement `AuthViewModel` with the Coroutine-wrapped Firebase Auth.
*   **Step 3**: Build the `LoginScreen` Compose UI, accurately matching the legacy visual style (rounded corners, background, Logo).
*   **Step 4**: Build the `DashboardScreen` Compose UI, wiring the four main buttons to their respective navigation events.
*   **Step 5**: Integrate the logic in `MainActivity` to dynamically set the initial start screen based on the Auth state.

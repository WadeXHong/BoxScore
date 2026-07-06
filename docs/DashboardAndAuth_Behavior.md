# Feature Behavior Specification: Dashboard & Authentication (`BoxScoreActivity`)

## 1. Feature Overview
The `BoxScoreActivity` serves as the central hub of the application. Surprisingly, it acts as **both** the main navigation dashboard (routing to Start Game, Team Manage, etc.) **and** the Authentication screen (Login / Sign Up). 

## 2. Navigation Flow & State Machine
Because the Auth and Dashboard flows are tightly coupled in the XML, the visibility or state of the buttons likely changes based on whether the user is logged in.

*   **Entry Point:** `LogoTransitionActivity` (Splash Screen) -> automatically routes to `BoxScoreActivity`.
*   **Authentication Flow**
    *   *Action:* User enters email in the Username field and password in the Password field.
    *   *Action:* Taps "Login" or "Sign up".
    *   *Note:* The actual `activity_login.xml` file is completely empty ("Hello World" placeholder), proving that `BoxScoreActivity` is doing the heavy lifting.
*   **Dashboard Flow (Main Navigation)**
    *   *Action:* Tap "Start Game" -> Navigates to `StartGameActivity`.
    *   *Action:* Tap "Team Manage" -> Navigates to `TeamManageActivity`.
    *   *Action:* Tap "Game History" -> Navigates to `GameHistoryActivity`.
    *   *Action:* Tap "Setting" -> Navigates to `SettingActivity`.

## 3. UI/UX & Call-to-Actions (CTAs)
*   **Activity Host (`activity_box_score.xml`)**
    *   **UI Elements:** A single large `ConstraintLayout` with a branded background (`@drawable/background_main`).
    *   **Header:** A massive logo at the top (`@drawable/logo_main`).
    *   **The Grid Layout:** The screen is divided into 4 horizontal sections, each containing a side-by-side split of a Navigation CTA and an Auth input/CTA:
        *   *Row 1:* [Start Game Button] | [Email EditText]
        *   *Row 2:* [Team Manage Button] | [Password EditText]
        *   *Row 3:* [Game History Button] | [Login Button]
        *   *Row 4:* [Setting Button] | [Sign Up Button]
    *   **Design Note:** The inputs and buttons use rounded shapes (`@drawable/shape_btn_main_ori` and `ripple_btn_main`).

## 4. Data Dependencies (Current State)
*   **Presenters:** `BoxScorePresenter` manages the click listeners and likely handles the authentication logic (API calls or local DB auth) alongside navigation intents.

## 5. Migration Notes for Compose / MVVM
*   **Separation of Concerns (CRITICAL):** This UI is highly unconventional. When refactoring to Jetpack Compose, we strongly recommend splitting this into two entirely separate screens/routes:
    1.  `LoginScreen` (Email, Password, Login, Sign Up).
    2.  `DashboardScreen` (Logo, Start Game, Team Manage, History, Settings).
*   **Routing:** A `NavHost` should evaluate a `UserSession` state (e.g., from DataStore). If logged out, show `LoginScreen`. If logged in, show `DashboardScreen`.
*   **UI Parity:** If the user explicitly wants to maintain this exact "split grid" UI where the login form lives next to the main menu, Compose `Row` and `Column` with `weight(1f)` modifiers can easily replicate this exact layout.

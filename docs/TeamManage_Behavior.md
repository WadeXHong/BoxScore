# Feature Behavior Specification: Team Management (`teammanage`)

## 1. Feature Overview
The **Team Management** module allows users to view, create, and manage teams and their associated player rosters. It operates as a Single-Activity feature hosted by `TeamManageActivity`, which swaps between several fragments to handle the different flows.

## 2. Navigation Flow & State Machine
The navigation is managed by `TeamManagePresenter`, utilizing standard `FragmentManager` transactions with custom slide animations.

*   **Entry Point:** `TeamManageActivity` -> Loads `TeamMainFragment`.
*   **State 1: Team Main List (`TeamMainFragment`)**
    *   Displays all existing teams.
    *   *Action:* Click "Create Team" -> Opens `CreateTeamDialogFragment`.
    *   *Action:* Click a specific Team -> Navigates to `TeamPlayersFragment` (passing the `teamId`).
*   **State 2: Create Team Dialog (`CreateTeamDialogFragment`)**
    *   A modal dialog overlay.
    *   *Action:* Submit -> Creates team in DB -> Dismisses dialog -> Triggers `refreshMainUi()` to update the Team List.
*   **State 3: Team Roster (`TeamPlayersFragment`)**
    *   Displays the list of players for the selected `teamId`.
    *   *Action:* Click "Add Player" -> Navigates to `CreatePlayerFragment` (passing the `teamId`).
    *   *Action:* Back Press -> Returns to `TeamMainFragment`.
*   **State 4: Create Player Form (`CreatePlayerFragment`)**
    *   A form to input new player details (Name, Number, Position, etc.).
    *   *Action:* Submit -> Creates player in DB -> Pops backstack returning to `TeamPlayersFragment`.

## 3. UI/UX & Call-to-Actions (CTAs)
The feature predominantly uses a dark theme (`@color/colorDarkBackground`) with high-contrast accent colors for buttons.

*   **Team Main Screen (`fragment_team_main.xml`)**
    *   **UI Elements:** A full-width `RecyclerView` displaying list items for existing teams.
    *   **Primary CTA:** A prominent header row spanning the top width with a white '+' icon (`ic_add_white_24dp`) and "Create Team" text. Tapping this triggers the Dialog.
*   **Create Team Dialog (`dialogfragment_create_team.xml`)**
    *   **UI Elements:** A centered white card overlay. Contains a title ("Please input team name"), a divider, and a single-line `EditText` (max length: 20 characters).
    *   **CTAs:** Two text-based buttons at the bottom: "Cancel" (dismisses without saving) and "Confirm" (validates input and saves to DB).
*   **Team Roster Screen (`fragment_team_players.xml`)**
    *   **UI Elements:** Mirrors the main screen design. A `RecyclerView` displaying players for the selected team.
    *   **Primary CTA:** A top header row with a '+' icon and "Create Player" text. Tapping navigates to the Create Player form.
*   **Create Player Form (`fragment_create_player.xml`)**
    *   **UI Elements:** A scrollable form with a dark background.
        *   *Player Name:* Single-line `EditText` (max 20 chars).
        *   *Player Number:* Numeric `EditText` (max 2 digits).
        *   *Positions:* A group of 5 CheckBoxes (C, PF, SF, SG, PG) allowing the user to select the player's roles.
    *   **Primary CTA:** A large "Confirm" button at the bottom of the form (`shape_btn_datarecord`).
*   **Activity Toolbar**
    *   The Activity dynamically manages the Top Toolbar to reflect the current step (e.g., swapping title to "Create Player" or showing the Team Name).

## 4. Business Logic (Presenter Layer)
*   **Fragment Routing (`TeamManagePresenter`):** Acts as a central router/coordinator for the entire Team Management flow. It manually orchestrates `FragmentTransaction`s (using `add`, `hide`, `show`, and `addToBackStack`) to navigate between the team list, player list, and creation forms.
*   **Toolbar Coordination:** Explicitly tells the host Activity which Toolbar configuration to display based on the currently visible fragment.

## 5. Data Dependencies (Current State)
*   **Presenters:** `TeamManagePresenter`, `TeamMainPresenter`, `TeamPlayersPresenter`, `CreateTeamPresenter`, `CreatePlayerPresenter`.
*   **Local Database:** Interacts with `TeamDbHelper` (and potentially `GameDataDbHelper`) to perform CRUD operations for Teams and Players.
*   **Data Passing:** The `teamId` (String) is the primary key passed between fragments (via `setTeamId()` methods) to maintain context.

## 5. Migration Notes for Compose / MVVM
*   **Navigation:** The Fragment backstack (`addToBackStack`) and Dialogs will be replaced by `NavHost` in Compose. `teamId` will be passed as a navigation argument.
*   **State Management:** The dynamic Toolbar updates should be hoisted to a shared `TeamManageViewModel` or handled via the current route in the `NavController`.
*   **Data:** `refreshMainUi()` callback chains will be replaced by observing a reactive `Flow<List<Team>>` from the Room Database, automatically updating the UI when a team is inserted via the Dialog.

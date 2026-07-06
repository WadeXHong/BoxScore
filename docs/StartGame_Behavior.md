# Feature Behavior Specification: Start Game (`startgame`)

## 1. Feature Overview
The **Start Game** module is a configuration wizard that allows users to set up a new basketball game before recording the box score. It uses a `ViewPager` hosted by `StartGameActivity` to navigate through three sequential setup steps: Game Info, Player Roster, and Detailed Rules.

## 2. Navigation Flow & State Machine
The navigation is horizontally paginated using a `ViewPager`. `StartGamePresenter` manages the validation and allows transition to the next page only if the current page's inputs are valid.

*   **Entry Point:** `StartGameActivity` -> Initializes `ViewPager` starting at Index 0.
*   **Step 1: Game Info Setup (`GameNameSettingFragment`)**
    *   *Action:* User selects their team, enters opponent name, game title, and date.
    *   *Validation:* Presenter calls `checkInput()`. If valid, allows swipe/transition to Step 2.
    *   *Edge Case:* If there are no teams in the DB, it triggers `noLegalTeam()` to warn the user.
*   **Step 2: Player Roster Selection (`PlayerListFragment`)**
    *   *Action:* User selects which players from the chosen team will participate in this game. 
    *   *Validation:* Ensures enough players are selected before moving to Step 3.
*   **Step 3: Detailed Rules Setup (`DetailSettingFragment`)**
    *   *Action:* User configures the technical rules of the game (quarter length, fouls, timeouts).
    *   *Final Action:* Submits the configuration -> Constructs a `GameInfo` object -> Navigates to `GameBoxScoreActivity` (Live Scorekeeping).

## 3. UI/UX & Call-to-Actions (CTAs)
*   **Activity Host (`activity_start_game.xml`)**
    *   The Activity dynamically manages the Top Toolbar to reflect the current step: `setGameNameSettingToolBar()`, `setPlayerListToolBar()`, `setDetailSettingToolBar()`. It likely contains a "Next" or "Start" CTA in the toolbar menu.
*   **Game Name Setting (`fragment_game_name_setting.xml`)**
    *   **UI Elements:** A scrollable form.
        *   *Your Team:* A `Spinner` dropdown populated from the database.
        *   *Game Title:* `EditText` (max length: 20 chars).
        *   *Opponent Name:* `EditText` (max length: 20 chars). Starts with a red/illegal background shape (`shape_edittext_gamename_illegal`) if empty or invalid.
        *   *Game Date:* A clickable `TextView` that likely triggers a DatePickerDialog.
*   **Player List (`fragment_playerlist.xml`)**
    *   **UI Elements:** A simple `RecyclerView`.
    *   **List Items:** Supports multiple view types (`item_playerlist_title`, `item_playerlist_unregisteredplayers`, `item_playerlist_players`) to categorize selected vs available players.
*   **Detail Setting (`fragment_detail_setting.xml`)**
    *   **UI Elements:** A scrollable form consisting of "stepper" controls (a numeric `TextView` sandwiched between `[ - ]` and `[ + ]` buttons).
    *   **Configurations:**
        *   *Quarter Length:* Default 10 minutes (max 2 digits).
        *   *Total Quarters:* Default 4 quarters (max 1 digit).
        *   *Max Fouls:* Default 5 times (max 1 digit).
        *   *Timeouts First Half:* Default 5 times (max 1 digit).
        *   *Timeouts Second Half:* Default 5 times (max 1 digit).

## 4. Business Logic (Presenter Layer)
*   **Wizard Validation (`StartGamePresenter`):** Controls the progression through the ViewPager. Before advancing to the next step, the presenter calls `checkInputIsLegal()` on the sub-presenters (e.g., `GameNameSettingPresenter`, `PlayerListPresenter`).
*   **Data Aggregation:** Collects the validated input from each of the three wizard fragments and compiles it into a single, unified `GameInfo` object at the end of the flow.

## 5. Data Dependencies (Current State)
*   **Presenters:** `StartGamePresenter` acts as the orchestrator for the wizard, gathering data from the fragments.
*   **Models:** The ultimate goal of this feature is to assemble and return a complete `GameInfo` object (`getSettingResult()`).
*   **Local Database:** Requires access to `TeamDbHelper` to populate the initial "Your Team" spinner and `PlayerList`.

## 5. Migration Notes for Compose / MVVM
*   **Navigation:** The `ViewPager` and Fragment adapters should be replaced by a `HorizontalPager` (from Accompanist/Compose Foundation) or a sequential `NavHost` flow.
*   **State Management:** The `GameInfo` object being constructed should live inside a scoped `StartGameViewModel` as a `StateFlow`. As the user progresses through the Pager, they modify this shared state.
*   **UI Parity:** The custom `[ - ]` and `[ + ]` stepper buttons in Step 3 need to be built as a reusable Composable component since they appear 5 times on the screen.

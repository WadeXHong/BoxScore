# Migration Plan: Start Game

## 1. Business Logic & Existing Behavior Analysis
`StartGameActivity` acts as a **"3-Step Configuration Wizard"** implemented via a `ViewPager`. Users must sequentially complete the setup and pass validations on each page to generate a `GameInfo` object before entering the live scoring screen.

### Step-by-Step Validation Details
*   **Dynamic Toolbar**: The Activity's top Toolbar dynamically changes its title based on the current step (Step 1~3) and provides a "Next/Start" action button in the menu.
*   **Step 1: Game Info Setup**
    *   **Your Team**: Fetched from the local database and populated into a Dropdown/Spinner. **Edge Case**: If the database has no teams, a `noLegalTeam()` warning must be triggered.
    *   **Game Title & Opponent Name**: Maximum length of 20 characters.
    *   **Validation**: The Opponent Name cannot be empty. If validation fails, the background turns into a red warning style (`shape_edittext_gamename_illegal`).
    *   **Date**: Clicking triggers a DatePickerDialog.
*   **Step 2: Player Roster Selection**
    *   Displays all players belonging to the team selected in Step 1.
    *   **List Logic**: Mixes different ViewTypes (Registered vs Unregistered players). Users select the active roster via Checkboxes. Validation ensures a minimum number of players are selected.
*   **Step 3: Detailed Rules Setup**
    *   Configures 5 core rules using a custom **Numeric Stepper (`[ - ] value [ + ]`)**:
        1. Quarter Length: Default 10 minutes, max 2 digits.
        2. Total Quarters: Default 4 quarters, max 1 digit.
        3. Max Fouls (Foul Out): Default 5 times, max 1 digit.
        4. Timeouts (1st Half): Default 5 times, max 1 digit.
        5. Timeouts (2nd Half): Default 5 times, max 1 digit.

## 2. Compose & Architectural Migration Strategy
We will replace the legacy Fragments and `ViewPager` with a Compose `HorizontalPager`, managed by a shared ViewModel that spans the entire wizard.

### UI Layer (Compose)
1.  **`StartGameScreen`**: The Host screen containing the dynamic TopAppBar (synchronized with the pager) and the `HorizontalPager`.
2.  **`GameNameSettingPage`**: Implements TextFields with red error states and the DatePicker.
3.  **`PlayerListPage`**: Uses `LazyColumn` for a multi-selectable roster list.
4.  **`DetailSettingPage`**: Extracts the `[ - ] [ value ] [ + ]` logic into a reusable `NumberStepper` Composable, repeating it 5 times for the rules.

### Presentation Layer (MVVM)
1.  **`StartGameViewModel`**:
    *   Lifecycle spans the entire wizard flow.
    *   Maintains a `MutableStateFlow<GameInfoBuilder>` collecting data across the 3 pages.
    *   **Business Logic**: Contains `validateStep1()` (handling red borders and empty warnings) and `validateStep2()`.
    *   **Final Output**: When Step 3 is confirmed, calls `buildGameInfo()` to output the final object for navigation to `GameBoxScore`.

## 3. Subagent Task Breakdown
*   **Step 1**: Implement Room DAO queries in `TeamDao` and `PlayerDao` to fetch the team list and their specific rosters.
*   **Step 2**: Build `StartGameViewModel` with the StateFlow data collector and step-by-step validation logic.
*   **Step 3**: Develop Compose UI for Step 1 (`GameNameSettingPage` with dropdown and red error borders) and Step 2 (`PlayerListPage` with checkboxes).
*   **Step 4**: Develop Compose UI for Step 3: encapsulate the `NumberStepper` component and build the `DetailSettingPage`.
*   **Step 5**: Integrate the `HorizontalPager` inside `StartGameScreen`, wiring up the Toolbar titles and validation checks for page swiping.

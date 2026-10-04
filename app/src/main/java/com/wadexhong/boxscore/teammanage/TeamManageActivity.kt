package com.wadexhong.boxscore.teammanage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.teammanage.createteam.CreateTeamScreen
import com.wadexhong.boxscore.teammanage.createteam.CreateTeamViewModel
import com.wadexhong.boxscore.teammanage.createteam.CreateTeamViewModelFactory
import com.wadexhong.boxscore.teammanage.teammain.TeamMainScreen
import com.wadexhong.boxscore.teammanage.teammain.TeamMainViewModel
import com.wadexhong.boxscore.teammanage.teammain.TeamMainViewModelFactory
import com.wadexhong.boxscore.teammanage.teamplayers.TeamPlayersScreen
import com.wadexhong.boxscore.teammanage.teamplayers.TeamPlayersViewModel
import com.wadexhong.boxscore.teammanage.teamplayers.TeamPlayersViewModelFactory
import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.CreatePlayerScreen
import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.CreatePlayerViewModel
import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.CreatePlayerViewModelFactory

/**
 * Compose host for team management (legacy TeamManageActivity + TeamManagePresenter
 * fragment transactions): teams -> players of a team -> create player, plus the create
 * team dialog.
 */
class TeamManageActivity : ComponentActivity() {

    private companion object {
        const val ARG_TEAM_ID = "teamId"
        const val ROUTE_TEAMS = "teams"
        const val ROUTE_PLAYERS = "players/{$ARG_TEAM_ID}"
        const val ROUTE_CREATE_PLAYER = "createPlayer/{$ARG_TEAM_ID}"
        const val ROUTE_CREATE_TEAM = "createTeam"
    }

    override fun onResume() {
        super.onResume()
        val layoutParams = window.attributes
        layoutParams.screenBrightness = BoxScore.sBrightness
        window.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = BoxScoreDatabase.getDatabase(applicationContext)
        val teamIdArg = listOf(navArgument(ARG_TEAM_ID) { type = NavType.StringType })

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                // The create team dialog sits on top of the teams list.
                val route = backStackEntry?.destination?.route.takeUnless { it == ROUTE_CREATE_TEAM } ?: ROUTE_TEAMS

                Scaffold(
                    modifier = Modifier.systemBarsPadding(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(
                                        when (route) {
                                            ROUTE_PLAYERS -> R.string.playerlist
                                            ROUTE_CREATE_PLAYER -> R.string.createPlayer
                                            else -> R.string.teamManage
                                        }
                                    ),
                                    color = Color.White
                                )
                            },
                            backgroundColor = colorResource(R.color.colorOrange),
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        painter = painterResource(
                                            if (route == ROUTE_TEAMS) R.drawable.ic_arrow_back_white_30dp
                                            else R.drawable.ic_keyboard_arrow_left_white_30dp
                                        ),
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(colorResource(R.color.colorDarkBackground))
                    ) {
                        NavHost(navController = navController, startDestination = ROUTE_TEAMS) {
                            composable(ROUTE_TEAMS) {
                                val vm: TeamMainViewModel = viewModel(factory = TeamMainViewModelFactory(db.teamDao()))
                                val uiState by vm.uiState.collectAsState()
                                TeamMainScreen(
                                    uiState = uiState,
                                    onCreateTeamClick = {
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) navController.navigate(ROUTE_CREATE_TEAM)
                                    },
                                    onTeamPlayersClick = { teamId ->
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) navController.navigate("players/$teamId")
                                    },
                                    onDeleteTeamClick = vm::deleteTeam
                                )
                            }
                            composable(ROUTE_PLAYERS, arguments = teamIdArg) { entry ->
                                val teamId = entry.arguments?.getString(ARG_TEAM_ID).orEmpty()
                                val vm: TeamPlayersViewModel =
                                    viewModel(factory = TeamPlayersViewModelFactory(teamId, db.playerDao()))
                                val uiState by vm.uiState.collectAsState()
                                TeamPlayersScreen(
                                    uiState = uiState,
                                    onCreatePlayerClick = {
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) navController.navigate("createPlayer/$teamId")
                                    },
                                    onDeletePlayerClick = vm::deletePlayer
                                )
                            }
                            // A dialog destination: its CreateTeamViewModel is cleared when it closes.
                            dialog(
                                ROUTE_CREATE_TEAM,
                                dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                            ) {
                                val vm: CreateTeamViewModel = viewModel(factory = CreateTeamViewModelFactory(db.teamDao()))
                                val uiState by vm.uiState.collectAsState()
                                CreateTeamScreen(
                                    uiState = uiState,
                                    onConfirmClick = { name ->
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) vm.createTeam(name)
                                    },
                                    onCancelClick = { navController.popBackStack() },
                                    onSuccess = { navController.popBackStack() },
                                    onErrorShown = vm::clearError
                                )
                            }
                            composable(ROUTE_CREATE_PLAYER, arguments = teamIdArg) { entry ->
                                val teamId = entry.arguments?.getString(ARG_TEAM_ID).orEmpty()
                                val vm: CreatePlayerViewModel =
                                    viewModel(factory = CreatePlayerViewModelFactory(teamId, db.playerDao()))
                                val uiState by vm.uiState.collectAsState()
                                val existingNumbers by vm.existingNumbers.collectAsState()
                                CreatePlayerScreen(
                                    uiState = uiState,
                                    existingNumbers = existingNumbers,
                                    onConfirmClick = { name, number, isC, isPF, isSF, isSG, isPG ->
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) {
                                            vm.createPlayer(name, number, isC, isPF, isSF, isSG, isPG)
                                        }
                                    },
                                    onSuccess = { navController.popBackStack() },
                                    onErrorShown = vm::clearError
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

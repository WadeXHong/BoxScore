package com.wadexhong.boxscore

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.wadexhong.boxscore.auth.LoginScreen
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.dashboard.DashboardScreen
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity
import com.wadexhong.boxscore.gamehistory.GameHistoryActivity
import com.wadexhong.boxscore.sync.FirebaseSync
import com.wadexhong.boxscore.setting.SettingActivity
import com.wadexhong.boxscore.startgame.StartGameActivity
import com.wadexhong.boxscore.teammanage.TeamManageActivity

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    /** Legacy BoxScoreActivity.onStart: show the login screen again after logging out in Settings. */
    override fun onStart() {
        super.onStart()
        val controller = navController ?: return
        if (FirebaseAuth.getInstance().currentUser == null &&
            controller.currentDestination?.route != "login"
        ) {
            controller.navigate("login") {
                popUpTo(controller.graph.id) { inclusive = true }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val layoutParams = window.attributes
        layoutParams.screenBrightness = BoxScore.sBrightness
        window.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // --- TEMP MOCK DATA SCRIPT ---
        // Debug builds only: seeds "Test Team" once per launch when there are no teams.
        // TODO: remove before release.
        if ((applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0) lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val db = com.wadexhong.boxscore.database.BoxScoreDatabase.getDatabase(this@MainActivity)
            if (db.teamDao().getAllTeams().first().isEmpty()) {
                val teamId = java.util.UUID.randomUUID().toString()
                db.teamDao().insertTeam(
                    com.wadexhong.boxscore.database.entity.TeamInfoEntity(
                        teamId = teamId,
                        teamName = "Test Team",
                        playersAmount = 6,
                        historyAmount = 0
                    )
                )
                for (i in 1..6) {
                    db.playerDao().insertPlayer(
                        com.wadexhong.boxscore.database.entity.TeamPlayerEntity(
                            playerId = java.util.UUID.randomUUID().toString(),
                            teamId = teamId,
                            playerName = "Player $i",
                            playerNumber = i.toString(),
                            center = 0,
                            powerForward = 0,
                            smallForward = 0,
                            shootingGuard = 0,
                            pointGuard = 0
                        )
                    )
                }
            }
        }
        // -----------------------------

        val currentUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (currentUser != null) {
            syncFromCloud()
            "dashboard"
        } else {
            "login"
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    this@MainActivity.navController = navController

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    syncFromCloud()
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("dashboard") {
                            DashboardScreen(
                                onStartGameClick = {
                                    handleStartGame()
                                },
                                onTeamManageClick = {
                                    startActivity(Intent(this@MainActivity, TeamManageActivity::class.java))
                                },
                                onGameHistoryClick = {
                                    startActivity(Intent(this@MainActivity, GameHistoryActivity::class.java))
                                },
                                onSettingClick = {
                                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                                    intent.putExtra(Constants.ExtraNames.SETTING_BOOLEAN_IS_SHOW_LOGOUT, true)
                                    startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private val database by lazy { BoxScoreDatabase.getDatabase(applicationContext) }
    private val gameRepository by lazy { GameRepository(database) }

    /** Legacy Get.onCreate: pull the signed-in user's cloud data into Room. */
    private fun syncFromCloud() {
        lifecycleScope.launch { FirebaseSync.download(database) }
    }

    /** Legacy BoxScorePresenter.checkUnfinishedGame: offer to resume a game left in progress. */
    private fun handleStartGame() {
        lifecycleScope.launch {
            val unfinished = gameRepository.getUnfinishedGame()
            if (unfinished == null) startNewGame() else askResumeGame(unfinished)
        }
    }

    private fun startNewGame() {
        startActivity(Intent(this, StartGameActivity::class.java))
    }

    private fun askResumeGame(game: GameInfoEntity) {
        AlertDialog.Builder(this, R.style.OrangeDialog)
            .setTitle(R.string.ask_resume_game_title)
            .setMessage(getString(R.string.ask_resume_game_message, game.opponentName))
            .setPositiveButton(getString(R.string.ask_resume_game_button_resume)) { dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    val gameInfo = gameRepository.loadGameInfo(game.gameId)
                    if (gameInfo == null) {
                        startNewGame()
                    } else {
                        startActivity(
                            Intent(this@MainActivity, GameBoxScoreActivity::class.java)
                                .putExtra(StartGameActivity.EXTRA_GAME_INFO, gameInfo)
                        )
                    }
                }
            }
            .setNegativeButton(getString(R.string.ask_resume_game_button_save_and_start)) { dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    if (gameRepository.finishGame(game.gameId) != null) {
                        FirebaseSync.uploadFinishedGame(database, game.gameId)
                    }
                    startNewGame()
                }
            }
            .setNeutralButton(getString(R.string.ask_resume_game_button_discard)) { dialog, _ ->
                dialog.dismiss()
                lifecycleScope.launch {
                    gameRepository.discardGame(game.gameId)
                    startNewGame()
                }
            }
            .show()
    }
}

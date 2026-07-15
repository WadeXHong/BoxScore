package com.wadexhong.boxscore

import android.app.AlertDialog
import android.content.Intent
import android.database.CursorIndexOutOfBoundsException
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.wadexhong.boxscore.auth.LoginScreen
import com.wadexhong.boxscore.dashboard.DashboardScreen
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity
import com.wadexhong.boxscore.gamehistory.GameHistoryActivity
import com.wadexhong.boxscore.modelhelper.SharedPreferenceHelper
import com.wadexhong.boxscore.modelhelper.firebasemodel.Create
import com.wadexhong.boxscore.modelhelper.firebasemodel.Get
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.setting.SettingActivity
import com.wadexhong.boxscore.startgame.StartGameActivity
import com.wadexhong.boxscore.teammanage.TeamManageActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val startDestination = if (currentUser != null) {
            Get.getInstance().onCreate()
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

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    Get.getInstance().onCreate()
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

    private fun handleStartGame() {
        if (SharedPreferenceHelper.contains(SharedPreferenceHelper.PLAYING_GAME)) {
            val gameId = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "")
            val cursor = BoxScore.getGameInfoDbHelper()
                .readableDatabase
                .query(
                    Constants.GameInfoDBContract.TABLE_NAME, null,
                    Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID + " = ?",
                    arrayOf(gameId), null, null, null
                )

            cursor.moveToFirst()
            try {
                val opponentName = cursor.getString(cursor.getColumnIndexOrThrow(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME))
                askResumeGame(opponentName)
            } catch (e: Exception) {
                Log.w("MainActivity", e.message ?: "")
                removeGameDataSharedPreferences()
                startActivity(Intent(this, StartGameActivity::class.java))
            }
            cursor.close()
        } else {
            startActivity(Intent(this, StartGameActivity::class.java))
        }
    }

    private fun askResumeGame(opponentName: String) {
        AlertDialog.Builder(this, R.style.OrangeDialog)
            .setTitle(R.string.ask_resume_game_title)
            .setMessage(getString(R.string.ask_resume_game_message, opponentName))
            .setPositiveButton(getString(R.string.ask_resume_game_button_resume)) { dialog, _ ->
                startActivity(Intent(this, GameBoxScoreActivity::class.java).putExtra("GameInfo", GameInfo()).putExtra("isResume", true))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.ask_resume_game_button_save_and_start)) { dialog, _ ->
                saveAndEndCurrentGame()
                startActivity(Intent(this, StartGameActivity::class.java))
                dialog.dismiss()
            }
            .setNeutralButton(getString(R.string.ask_resume_game_button_discard)) { dialog, _ ->
                removeGameDataInDataBase()
                removeGameDataSharedPreferences()
                startActivity(Intent(this, StartGameActivity::class.java))
                dialog.dismiss()
            }
            .show()
    }

    private fun removeGameDataSharedPreferences() {
        SharedPreferenceHelper.remove(SharedPreferenceHelper.PLAYING_GAME)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_ID)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_FOUL)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_FOUL)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.YOUR_TEAM_TOTAL_SCORE)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.OPPONENT_TEAM_TOTAL_SCORE)
        SharedPreferenceHelper.remove(SharedPreferenceHelper.QUARTER)
    }

    private fun removeGameDataInDataBase() {
        val gameId = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "")
        BoxScore.getGameInfoDbHelper().deleteGameInfo(null, gameId)
        BoxScore.getGameDataDbHelper().deleteGameData(gameId)
    }

    private fun saveAndEndCurrentGame() {
        val gameId = SharedPreferenceHelper.read(SharedPreferenceHelper.PLAYING_GAME, "")
        val teamId = SharedPreferenceHelper.read(SharedPreferenceHelper.YOUR_TEAM_ID, "")

        if (gameId.isNotEmpty() && teamId.isNotEmpty()) {
            val newHistoryAmount = BoxScore.getGameInfoDbHelper().overPlayingGame(gameId, teamId)
            BoxScore.getTeamDbHelper().updateHistoryAmount(teamId, newHistoryAmount)

            if (FirebaseAuth.getInstance().currentUser != null) {
                Create.getInstance().createGameData(BoxScore.getGameDataDbHelper().getSpecificGameData(gameId))
                Create.getInstance().createGameInfo(BoxScore.getGameInfoDbHelper().getSpecificInfo(gameId))
                Create.getInstance().updateTeamHistoryAmount(teamId, newHistoryAmount)
            }
        }
        removeGameDataSharedPreferences()
    }
}

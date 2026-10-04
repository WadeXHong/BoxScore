package com.wadexhong.boxscore.startgame

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity
import com.wadexhong.boxscore.teammanage.TeamManageActivity

class StartGameActivity : ComponentActivity() {

    companion object {
        const val EXTRA_GAME_INFO = "GameInfo"
    }

    private val viewModel: StartGameViewModel by viewModels {
        val database = BoxScoreDatabase.getDatabase(BoxScore.getAppContext())
        StartGameViewModelFactory(database.teamDao(), database.playerDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (BoxScore.sBrightness != -1f) {
            val layoutParams = window.attributes
            layoutParams.screenBrightness = BoxScore.sBrightness
            window.attributes = layoutParams
        }

        setContent {
            StartGameScreen(
                viewModel = viewModel,
                onBack = { finish() },
                onStartGame = { gameInfo ->
                    if (BoxScore.isOnClickAllowedAndSetTimer()) {
                        val intent = Intent(this, GameBoxScoreActivity::class.java)
                        intent.putExtra(EXTRA_GAME_INFO, gameInfo)
                        startActivity(intent)
                        finish()
                    }
                },
                onNoTeams = {
                    startActivity(Intent(this, TeamManageActivity::class.java))
                    finish()
                }
            )
        }
    }
}

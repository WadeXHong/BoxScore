package com.wadexhong.boxscore.gameboxscore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.setting.SettingActivity
import com.wadexhong.boxscore.setting.SettingsRepository
import com.wadexhong.boxscore.setting.dataStore
import kotlinx.coroutines.launch

class GameBoxScoreActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        // Re-apply the brightness chosen in Settings (also after returning from SettingActivity).
        val layoutParams = window.attributes
        layoutParams.screenBrightness = BoxScore.sBrightness
        window.attributes = layoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gameInfo = intent.getSerializableExtra("GameInfo") as? GameInfo
        val gameId = gameInfo?.gameId ?: ""
        val teamId = gameInfo?.yourTeamId ?: ""

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = BoxScoreDatabase.getDatabase(applicationContext)
                val settings = SettingsRepository(applicationContext.dataStore)
                return GameBoxScoreViewModel(db, gameId, teamId, gameInfo, settings) as T
            }
        }

        setContent {
            val viewModel: GameBoxScoreViewModel = viewModel(factory = factory)
            GameBoxScoreScreen(
                viewModel = viewModel,
                onExit = { save ->
                    if (save) {
                        finish()
                    } else {
                        lifecycleScope.launch {
                            val db = BoxScoreDatabase.getDatabase(applicationContext)
                            db.gameEventDao().deleteEventsByGameId(gameId)
                            db.gameDao().deleteGameCompletely(gameId)
                            finish()
                        }
                    }
                },
                onEndGame = {
                    // TODO: legacy also marks the game over, bumps the team's history count and
                    //  uploads to Firebase. That needs game_info/game_data to be written, which is
                    //  pending the GameBoxScore <-> legacy DB data-layer decision.
                    finish()
                },
                onOpenSettings = {
                    if (BoxScore.isOnClickAllowedAndSetTimer()) {
                        startActivity(
                            Intent(this, SettingActivity::class.java)
                                .putExtra(Constants.ExtraNames.SETTING_BOOLEAN_IS_SHOW_LOGOUT, false)
                        )
                    }
                }
            )
        }
    }
}

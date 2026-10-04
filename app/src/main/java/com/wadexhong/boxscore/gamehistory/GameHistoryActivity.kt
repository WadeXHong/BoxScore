package com.wadexhong.boxscore.gamehistory

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.sync.FirebaseSync
import java.io.File

/** Compose replacement for legacy GameHistoryActivity: game list -> game detail. */
class GameHistoryActivity : ComponentActivity() {

    private companion object {
        const val ROUTE_LIST = "list"
        const val ARG_GAME_ID = "gameId"
        const val ROUTE_DETAIL = "detail/{$ARG_GAME_ID}"
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
        val listFactory = GameHistoryListViewModelFactory(
            gameDao = db.gameDao(),
            repository = GameRepository(db),
            exportDirectory = File(cacheDir, "xls"),
            uploader = ExcelUploader { gameId, file -> FirebaseSync.uploadExcel(gameId, file) },
            onGameDeleted = { _, _, _ -> }
        )

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val isDetail = backStackEntry?.destination?.route == ROUTE_DETAIL

                Scaffold(
                    modifier = Modifier.systemBarsPadding(),
                    topBar = {
                        TopAppBar(
                            title = { Text(if (isDetail) "比賽內容" else "歷史紀錄", color = Color.White) },
                            backgroundColor = colorResource(R.color.colorOrange),
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        painter = painterResource(
                                            if (isDetail) R.drawable.ic_keyboard_arrow_left_white_30dp
                                            else R.drawable.ic_arrow_back_white_30dp
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
                        NavHost(navController = navController, startDestination = ROUTE_LIST) {
                            composable(ROUTE_LIST) {
                                GameHistoryListScreen(
                                    viewModel = viewModel(factory = listFactory),
                                    onGameClick = { game ->
                                        if (BoxScore.isOnClickAllowedAndSetTimer()) {
                                            navController.navigate("detail/${game.gameId}")
                                        }
                                    }
                                )
                            }
                            composable(
                                ROUTE_DETAIL,
                                arguments = listOf(navArgument(ARG_GAME_ID) { type = NavType.StringType })
                            ) { entry ->
                                val gameId = entry.arguments?.getString(ARG_GAME_ID).orEmpty()
                                HistoryDetailScreen(
                                    viewModel = viewModel(factory = HistoryDetailViewModelFactory(db.gameDao(), gameId))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

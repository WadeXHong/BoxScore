package com.wadexhong.boxscore.gamehistory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class GameHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "historyList") {
                    composable("historyList") {
                        GameHistoryListScreen(
                            onNavigateToDetail = { gameId ->
                                navController.navigate("historyDetail/$gameId")
                            },
                            onBack = { finish() }
                        )
                    }
                    composable("historyDetail/{gameId}") { backStackEntry ->
                        val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
                        GameHistoryDetailScreen(
                            gameId = gameId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

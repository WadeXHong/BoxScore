package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun GameBoxScoreScreen(viewModel: GameBoxScoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Scoreboard
        ScoreboardHeader(uiState)

        // Bottom Action Tabs (Pager or TabRow)
        ActionTabs(viewModel, uiState)
    }
}

@Composable
fun ScoreboardHeader(uiState: GameUiState) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Your Score: ${uiState.yourScore}")
        Spacer(Modifier.weight(1f))
        Text("Quarter: ${uiState.quarter}")
        Spacer(Modifier.weight(1f))
        Text("Opponent: ${uiState.opponentScore}")
    }
}

@Composable
fun ActionTabs(viewModel: GameBoxScoreViewModel, uiState: GameUiState) {
    // Stub for ViewPager tabs: Record, Change Player, Undo
    Text("Tabs Content Placeholder")
}

// Step 2: Custom Multi-Finger Swipe Modifier
fun Modifier.multiFingerSwipe(onSwipe: (fingers: Int, direction: String) -> Unit): Modifier = this.pointerInput(Unit) {
    // Simple stub to represent the gesture detection logic
    // awaitEachGesture { ... }
}

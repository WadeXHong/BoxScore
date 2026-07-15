package com.wadexhong.boxscore.startgame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlayerListPage(viewModel: StartGameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uiState.isStep2Error) {
            Text("Please select at least 6 players (5 starters, 1 sub)", color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.players) { player ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.selectedPlayerIds.contains(player.playerId),
                        onCheckedChange = { isChecked ->
                            viewModel.onPlayerSelected(player.playerId, isChecked)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${player.playerNumber} - ${player.playerName}")
                }
            }
        }
    }
}

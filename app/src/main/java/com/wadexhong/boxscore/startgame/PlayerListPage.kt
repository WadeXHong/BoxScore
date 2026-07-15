package com.wadexhong.boxscore.startgame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun PlayerListPage(viewModel: StartGameViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uiState.isStep2Error) {
            Text("Please select exactly 5 starters and at least 1 sub", color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val starters = uiState.players.filter { uiState.startingPlayerIds.contains(it.playerId) }
            val subs = uiState.players.filter { uiState.substitutePlayerIds.contains(it.playerId) }
            val unregistered = uiState.players.filter { 
                !uiState.startingPlayerIds.contains(it.playerId) && !uiState.substitutePlayerIds.contains(it.playerId) 
            }

            // Starters Section
            item {
                val titleColor = if (starters.size == 5) Color(0xFF4CAF50) else MaterialTheme.colors.secondary
                val numberColor = if (starters.size == 5) Color(0xFF4CAF50) else Color.Red
                
                Text(
                    text = androidx.compose.ui.text.buildAnnotatedString {
                        withStyle(androidx.compose.ui.text.SpanStyle(color = titleColor)) {
                            append("先發球員 (")
                        }
                        withStyle(androidx.compose.ui.text.SpanStyle(color = numberColor)) {
                            append("${starters.size}")
                        }
                        withStyle(androidx.compose.ui.text.SpanStyle(color = titleColor)) {
                            append("/5)")
                        }
                    }, 
                    style = MaterialTheme.typography.h6
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            items(starters) { player ->
                PlayerRow(
                    player = player,
                    actionText = "替補",
                    onActionClick = { viewModel.moveToSubstitute(player.playerId) },
                    onRemove = { viewModel.removeFromRoster(player.playerId) }
                )
            }

            // Subs Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "替補球員 (${subs.size})", 
                    style = MaterialTheme.typography.h6, 
                    color = if (subs.isNotEmpty()) Color(0xFF4CAF50) else MaterialTheme.colors.secondary
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            items(subs) { player ->
                PlayerRow(
                    player = player,
                    actionText = "先發",
                    onActionClick = { viewModel.moveToStarting(player.playerId) },
                    onRemove = { viewModel.removeFromRoster(player.playerId) }
                )
            }

            // Unregistered Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "未登錄球員", 
                    style = MaterialTheme.typography.h6, 
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
            items(unregistered) { player ->
                val addActionText = if (starters.size < 5) "先發" else "替補"
                PlayerRow(
                    player = player,
                    actionText = addActionText,
                    onActionClick = { 
                        if (starters.size < 5) viewModel.moveToStarting(player.playerId) 
                        else viewModel.moveToSubstitute(player.playerId) 
                    },
                    onRemove = null
                )
            }
        }
    }
}

@Composable
fun PlayerRow(
    player: TeamPlayerEntity,
    actionText: String,
    onActionClick: () -> Unit,
    onRemove: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${player.playerNumber} - ${player.playerName}", modifier = Modifier.weight(1f))
        
        OutlinedButton(onClick = onActionClick) {
            Text(actionText)
        }
        
        if (onRemove != null) {
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Red)
            }
        } else {
            Spacer(modifier = Modifier.width(56.dp)) // To align buttons since there's no remove icon
        }
    }
}

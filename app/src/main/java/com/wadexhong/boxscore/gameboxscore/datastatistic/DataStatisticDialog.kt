package com.wadexhong.boxscore.gameboxscore.datastatistic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun DataStatisticDialog(
    players: List<TeamPlayerEntity>,
    onDismiss: () -> Unit
) {
    // Step 4: Complex UI - 2D scrollable spreadsheet
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        },
        title = { Text("Data Statistics") },
        text = {
            LazyColumn {
                item {
                    LazyRow {
                        item { Text("Player | PTS | FGM | FGA | 3PM ...") }
                    }
                }
                items(players.size) { index ->
                    LazyRow {
                        item { Text("${players[index].playerName} | 0 | 0 | 0 ...") }
                    }
                }
            }
        }
    )
}

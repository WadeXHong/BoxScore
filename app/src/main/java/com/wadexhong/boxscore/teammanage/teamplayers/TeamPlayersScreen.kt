package com.wadexhong.boxscore.teammanage.teamplayers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TeamPlayersScreen(
    uiState: TeamPlayersUiState,
    onCreatePlayerClick: () -> Unit,
    onDeletePlayerClick: (String) -> Unit
) {
    var playerToDelete by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDarkBackground))
    ) {
        // Create Player Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable { onCreatePlayerClick() }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Player",
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.createPlayer),
                fontSize = 20.sp,
                color = colorResource(id = R.color.colorButtonInMain)
            )
        }

        // Players List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.players) { player ->
                TeamPlayerListItem(
                    player = player,
                    onDeleteClick = { playerToDelete = player.playerId }
                )
            }
        }
    }

    // Delete Confirmation Dialog
    if (playerToDelete != null) {
        AlertDialog(
            onDismissRequest = { playerToDelete = null },
            title = { Text(stringResource(id = R.string.dialog_delete_player_title)) },
            text = { Text(stringResource(id = R.string.dialog_delete_player_message)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeletePlayerClick(playerToDelete!!)
                    playerToDelete = null
                }) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { playerToDelete = null }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun TeamPlayerListItem(
    player: TeamPlayerEntity,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = player.playerNumber,
            modifier = Modifier.width(75.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.colorButtonInMain),
            fontSize = 20.sp
        )
        Text(
            text = player.playerName,
            modifier = Modifier.weight(1f),
            color = colorResource(id = R.color.colorButtonInMain),
            fontSize = 20.sp
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_delete_forever_white_24dp),
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier
                .size(35.dp)
                .clickable { onDeleteClick() }
        )
        Spacer(modifier = Modifier.width(18.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TeamPlayersScreenPreview() {
    val samplePlayers = listOf(
        TeamPlayerEntity(
            teamId = "1", playerId = "p1", playerName = "LeBron James", playerNumber = "23",
            center = 0, powerForward = 1, smallForward = 1, shootingGuard = 0, pointGuard = 1
        ),
        TeamPlayerEntity(
            teamId = "1", playerId = "p2", playerName = "Anthony Davis", playerNumber = "3",
            center = 1, powerForward = 1, smallForward = 0, shootingGuard = 0, pointGuard = 0
        )
    )
    val uiState = TeamPlayersUiState(
        players = samplePlayers,
        isLoading = false
    )
    TeamPlayersScreen(
        uiState = uiState,
        onCreatePlayerClick = {},
        onDeletePlayerClick = {}
    )
}

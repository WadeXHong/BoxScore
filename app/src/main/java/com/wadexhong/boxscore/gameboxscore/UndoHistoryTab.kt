package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun UndoHistoryTab(
    actionHistory: List<GameEventEntity>,
    players: List<TeamPlayerEntity>,
    onUndoAction: (Long) -> Unit,
    onEditAction: (event: GameEventEntity, playerId: String, type: Int) -> Unit = { _, _, _ -> }
) {
    var menuEvent by remember { mutableStateOf<GameEventEntity?>(null) }
    var eventToEdit by remember { mutableStateOf<GameEventEntity?>(null) }
    var eventToUndo by remember { mutableStateOf<GameEventEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorDarkBackground))
            .padding(10.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Q", modifier = Modifier.weight(0.8f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
            Text(text = "No.", modifier = Modifier.weight(0.8f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
            Text(text = "Name", modifier = Modifier.weight(1.5f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
            Text(text = "Type", modifier = Modifier.weight(1.7f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(actionHistory.reversed()) { event ->
                val player = players.find { it.playerId == event.playerId }
                val typeString = recordTypeLabel(event.actionType)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 2.dp)
                        .background(colorResource(id = R.color.colorDarkGray))
                        .clickable { menuEvent = event }
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = event.quarter.toString(), modifier = Modifier.weight(0.8f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
                    Text(text = player?.playerNumber ?: "-", modifier = Modifier.weight(0.8f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
                    Text(text = player?.playerName ?: "Team", modifier = Modifier.weight(1.5f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
                    Text(text = typeString, modifier = Modifier.weight(1.7f), color = colorResource(id = R.color.colorButtonInMain), fontSize = 20.sp)
                }
            }
        }
    }

    menuEvent?.let { event ->
        UndoActionMenuDialog(
            onDismiss = { menuEvent = null },
            onEdit = {
                menuEvent = null
                eventToEdit = event
            },
            onDelete = {
                menuEvent = null
                eventToUndo = event
            }
        )
    }

    eventToEdit?.let { event ->
        UndoEditDialog(
            event = event,
            players = players,
            onDismiss = { eventToEdit = null },
            onConfirm = { playerId, type ->
                onEditAction(event, playerId, type)
                eventToEdit = null
            }
        )
    }

    eventToUndo?.let { event ->
        val player = players.find { it.playerId == event.playerId }
        AlertDialog(
            onDismissRequest = { eventToUndo = null },
            title = { Text(text = "復原確認") },
            text = {
                Text(
                    text = "\n是否要對\n\n「 ${player?.playerName ?: ""} ${recordTypeLabel(event.actionType)} 」\n\n進行復原 ?\n\n警告:\n　　 復原後將清除該項紀錄且不會另行顯示更動內容 !\n"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onUndoAction(event.id)
                    eventToUndo = null
                }) {
                    Text(stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { eventToUndo = null }) {
                    Text(stringResource(id = R.string.no))
                }
            }
        )
    }
}

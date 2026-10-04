package com.wadexhong.boxscore.gameboxscore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

/** Stat types selectable in make-up / edit dialogs (legacy `SelectTypeAdapter`). */
private val SELECTABLE_TYPES: List<Int> = Constants.TYPE_CHOICE_INT.drop(1)

@Composable
fun recordTypeLabel(type: Int): String {
    val titleId = Constants.TITLE_SPARSE_ARRAY.get(type)
    return if (titleId != null) stringResource(id = titleId) else ""
}

private fun TeamPlayerEntity.label() = "$playerNumber  $playerName"

/** Compose replacement for the legacy Spinner: a button that opens a dropdown of [options]. */
@Composable
private fun <T> OptionSelector(
    label: String,
    options: List<T>,
    selected: T?,
    optionLabel: @Composable (T) -> String,
    onSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label)
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selected?.let { optionLabel(it) } ?: "")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option)) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/** Legacy `MakeUpDialog`: record a stat for a player in an already finished quarter. */
@Composable
fun MakeUpDialog(
    currentQuarter: Int,
    players: List<TeamPlayerEntity>,
    onDismiss: () -> Unit,
    onConfirm: (playerId: String, type: Int, quarter: Int) -> Unit
) {
    val quarters = (1 until currentQuarter).toList()
    var quarter by remember { mutableStateOf(quarters.firstOrNull()) }
    var type by remember { mutableStateOf(SELECTABLE_TYPES.first()) }
    var player by remember { mutableStateOf(players.firstOrNull()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("補登數據") },
        text = {
            Column {
                Text("請選擇補登內容")
                OptionSelector("節數", quarters, quarter, { it.toString() }) { quarter = it }
                OptionSelector("項目", SELECTABLE_TYPES, type, { recordTypeLabel(it) }) { type = it }
                OptionSelector("球員", players, player, { it.label() }) { player = it }
            }
        },
        confirmButton = {
            TextButton(
                enabled = quarter != null && player != null,
                onClick = {
                    val q = quarter
                    val p = player
                    if (q != null && p != null) onConfirm(p.playerId, type, q)
                }
            ) { Text(stringResource(R.string.yes)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

/** Legacy undo-history item menu: 編輯 / 刪除 (the legacy "標記" entry did nothing). */
@Composable
fun UndoActionMenuDialog(
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("操作選單") },
        text = {
            Column {
                TextButton(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("編輯") }
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("刪除") }
            }
        },
        confirmButton = {}
    )
}

/** Legacy undo-history `EditDialog`: change the player and/or stat type of a recorded event. */
@Composable
fun UndoEditDialog(
    event: GameEventEntity,
    players: List<TeamPlayerEntity>,
    onDismiss: () -> Unit,
    onConfirm: (playerId: String, type: Int) -> Unit
) {
    val originalPlayer = players.find { it.playerId == event.playerId }
    var type by remember { mutableStateOf(if (event.actionType in SELECTABLE_TYPES) event.actionType else SELECTABLE_TYPES.first()) }
    var player by remember { mutableStateOf(originalPlayer ?: players.firstOrNull()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("變更選項") },
        text = {
            Column {
                Text("將　${originalPlayer?.playerNumber ?: "-"}號  –  ${recordTypeLabel(event.actionType)}\n\n變更為：")
                OptionSelector("項目", SELECTABLE_TYPES, type, { recordTypeLabel(it) }) { type = it }
                OptionSelector("球員", players, player, { it.label() }) { player = it }
            }
        },
        confirmButton = {
            TextButton(
                enabled = player != null,
                onClick = { player?.let { onConfirm(it.playerId, type) } }
            ) { Text(stringResource(R.string.yes)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

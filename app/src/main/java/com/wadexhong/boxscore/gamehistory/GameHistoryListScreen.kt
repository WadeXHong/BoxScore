package com.wadexhong.boxscore.gamehistory

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.GameInfoEntity

@Composable
fun GameHistoryListScreen(
    viewModel: GameHistoryListViewModel,
    onGameClick: (GameInfoEntity) -> Unit
) {
    val games by viewModel.games.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val context = LocalContext.current
    val textColor = colorResource(R.color.colorButtonInMain)

    var menuGame by remember { mutableStateOf<GameInfoEntity?>(null) }
    var shareGame by remember { mutableStateOf<GameInfoEntity?>(null) }
    var deleteGame by remember { mutableStateOf<GameInfoEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HistoryEvent.Shared -> {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("xls file url", event.url))
                    Toast.makeText(context, R.string.toast_url_in_clipboard, Toast.LENGTH_SHORT).show()
                }
                HistoryEvent.CannotCreateExcel ->
                    Toast.makeText(context, R.string.toast_cannot_create_excel, Toast.LENGTH_SHORT).show()
                // Legacy only logged upload failures.
                HistoryEvent.ShareFailed -> Unit
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RowCell("比賽日期/名稱", textColor)
            RowCell("我方隊伍", textColor)
            RowCell("比分", textColor)
            RowCell("對方隊伍", textColor)
            Box(modifier = Modifier.width(MORE_WIDTH))
        }
        Divider(color = textColor.copy(alpha = 0.3f))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(games, key = { it.gameId }) { game ->
                GameHistoryRow(
                    game = game,
                    textColor = textColor,
                    onClick = { onGameClick(game) },
                    onMoreClick = { menuGame = game }
                )
                Divider(color = textColor.copy(alpha = 0.3f))
            }
        }
    }

    menuGame?.let { game ->
        AlertDialog(
            onDismissRequest = { menuGame = null },
            title = { Text("更多選項") },
            text = {
                Column {
                    TextButton(
                        onClick = { menuGame = null; shareGame = game },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.share)) }
                    TextButton(
                        onClick = { menuGame = null; deleteGame = game },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.delete)) }
                }
            },
            buttons = {}
        )
    }

    shareGame?.let { game ->
        ConfirmDialog(
            title = "分享確認",
            message = "是否將此場比賽轉檔成.xls 並上傳至雲端？",
            onConfirm = { shareGame = null; viewModel.shareGame(game) },
            onDismiss = { shareGame = null }
        )
    }

    deleteGame?.let { game ->
        ConfirmDialog(
            title = "刪除確認",
            message = "是否將本場比賽永久刪除？",
            onConfirm = { deleteGame = null; viewModel.deleteGame(game) },
            onDismiss = { deleteGame = null }
        )
    }

    if (isUploading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Row(
                modifier = Modifier.background(Color.White).padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(color = colorResource(R.color.colorOrange))
                Text("上傳中...")
            }
        }
    }
}

private val MORE_WIDTH = 48.dp

@Composable
private fun GameHistoryRow(
    game: GameInfoEntity,
    textColor: Color,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(game.gameDate, color = textColor, fontSize = 11.sp)
            Text(
                game.gameName.ifEmpty { stringResource(R.string.gametitleDefault) },
                color = textColor,
                fontSize = 11.sp
            )
        }
        RowCell(stringResource(R.string.yourTeamName, game.yourTeamName), textColor)
        RowCell(stringResource(R.string.finalScore, game.yourScore, game.opponentScore), textColor)
        RowCell(stringResource(R.string.opponentTeamName, game.opponentName), textColor)
        Text(
            text = "更多",
            color = textColor,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(MORE_WIDTH)
                .clickable(onClick = onMoreClick)
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun RowScope.RowCell(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f)
    )
}

@Composable
internal fun ConfirmDialog(title: String, message: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.yes)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.no)) } }
    )
}

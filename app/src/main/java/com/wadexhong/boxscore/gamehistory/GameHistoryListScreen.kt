package com.wadexhong.boxscore.gamehistory

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wadexhong.boxscore.database.entity.GameInfoEntity

@Composable
fun GameHistoryListScreen(
    onNavigateToDetail: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: GameHistoryListViewModel = viewModel()
) {
    val gameInfos by viewModel.gameInfos.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("歷史紀錄", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = Color(0xFFF4511E) // Orange
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(gameInfos) { game ->
                GameHistoryRow(
                    game = game,
                    onClick = { onNavigateToDetail(game.gameId) },
                    onShare = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Game History: ${game.gameName}")
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                )
                Divider()
            }
        }
    }
}

@Composable
fun GameHistoryRow(game: GameInfoEntity, onClick: () -> Unit, onShare: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = game.gameDate, style = MaterialTheme.typography.caption)
            Text(text = game.gameName, style = MaterialTheme.typography.subtitle1)
            Text(text = "${game.yourTeamName} vs ${game.opponentName}")
            Text(text = "${game.yourScore} : ${game.opponentScore}")
        }
        TextButton(onClick = onShare) {
            Text("更多", color = Color(0xFFF4511E))
        }
    }
}

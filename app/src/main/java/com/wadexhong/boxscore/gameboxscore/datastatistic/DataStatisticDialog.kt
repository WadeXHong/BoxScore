package com.wadexhong.boxscore.gameboxscore.datastatistic

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Composable
fun DataStatisticDialog(
    players: List<TeamPlayerEntity>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorDialogBackground))
        ) {
            // Top Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Go Back",
                    fontSize = 60.sp,
                    color = colorResource(id = R.color.colorButtonInMain)
                )
            }

            // Bottom Section (Table)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .background(colorResource(id = R.color.colorOrange))
            ) {
                val scrollState = rememberScrollState()
                val stickyHeaders = listOf("No", "Name")
                val scrollableHeaders = listOf("PTS", "FGM", "FGA", "3PM", "3PA", "FTM", "FTA", "OREB", "DREB", "AST", "STL", "BLK", "TOV", "PF")
                
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Row (Vertically Sticky)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Horizontally Sticky
                        Row {
                            stickyHeaders.forEach { header ->
                                TableCell(text = header, width = 60.dp)
                            }
                        }
                        // Scrollable
                        Row(modifier = Modifier.horizontalScroll(scrollState)) {
                            scrollableHeaders.forEach { header ->
                                TableCell(text = header, width = 80.dp)
                            }
                        }
                    }
                    
                    // Data Rows
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(players) { player ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                // Horizontally Sticky
                                Row {
                                    TableCell(text = player.playerNumber, width = 60.dp)
                                    TableCell(text = player.playerName, width = 60.dp)
                                }
                                // Scrollable
                                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                                    repeat(scrollableHeaders.size) {
                                        TableCell(text = "0", width = 80.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableCell(text: String, width: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(50.dp)
            .padding(1.dp)
            .background(colorResource(id = R.color.colorDarkBackground)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.colorButtonInMain),
            fontSize = 18.sp
        )
    }
}

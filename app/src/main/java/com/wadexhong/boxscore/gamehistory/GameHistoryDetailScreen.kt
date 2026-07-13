package com.wadexhong.boxscore.gamehistory

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun GameHistoryDetailScreen(
    gameId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: GameHistoryDetailViewModel = viewModel(
        factory = GameHistoryDetailViewModelFactory(application, gameId)
    )
    
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Team Data", "Player Data")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("比賽內容", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = Color(0xFFF4511E)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.White,
                contentColor = Color(0xFFF4511E),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color(0xFFF4511E)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> TeamDataTab(uiState)
                    1 -> PlayerDataTab(uiState)
                }
            }
        }
    }
}

@Composable
fun TeamDataTab(uiState: DetailUiState) {
    val info = uiState.gameInfo ?: return
    val stats = uiState.teamStats

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(info.yourTeamName, fontWeight = FontWeight.Bold)
                    Text(info.yourScore.toString(), fontSize = 32.sp, color = if(info.yourScore >= info.opponentScore) Color.Red else Color.Black)
                }
                Text("vs", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(info.opponentName, fontWeight = FontWeight.Bold)
                    Text(info.opponentScore.toString(), fontSize = 32.sp, color = if(info.opponentScore > info.yourScore) Color.Red else Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text("Shooting Percentages", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val fgPct = if (stats.totalFga > 0) (stats.totalFgm.toFloat() / stats.totalFga) else 0f
            val tpPct = if (stats.total3pa > 0) (stats.total3pm.toFloat() / stats.total3pa) else 0f
            val ftPct = if (stats.totalFta > 0) (stats.totalFtm.toFloat() / stats.totalFta) else 0f

            ProgressBarItem("FG%", fgPct, "${stats.totalFgm}/${stats.totalFga}")
            ProgressBarItem("3P%", tpPct, "${stats.total3pm}/${stats.total3pa}")
            ProgressBarItem("FT%", ftPct, "${stats.totalFtm}/${stats.totalFta}")
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text("Other Stats (Your Team)", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            StatRow("Total Rebounds", stats.totalReb.toString())
            StatRow("Total Assists", stats.totalAst.toString())
        }
    }
}

@Composable
fun ProgressBarItem(label: String, percentage: Float, textValue: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label)
            Text("${(percentage * 100).toInt()}% ($textValue)")
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = Color(0xFFF4511E),
            backgroundColor = Color.LightGray
        )
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PlayerDataTab(uiState: DetailUiState) {
    GameBoxScoreSpreadsheet(gameDataList = uiState.gameDataList)
}

package com.wadexhong.boxscore.gamehistory

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.GameDataEntity
import kotlinx.coroutines.launch

private val QUARTER_FILTER_LABELS = listOf(
    R.string.total, R.string.firstQuarter, R.string.secondQuarter, R.string.thirdQuarter, R.string.forthQuarter
)

@Composable
fun HistoryDetailScreen(viewModel: HistoryDetailViewModel) {
    val state by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    val tabs = listOf(R.string.teamBoxScore, R.string.playersBoxscore)

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = colorResource(R.color.colorDarkBackground),
            contentColor = colorResource(R.color.colorButtonInMain),
            indicator = { positions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(positions[pagerState.currentPage]),
                    color = colorResource(R.color.secondaryDarkColor)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(stringResource(title)) }
                )
            }
        }

        // Legacy NonSwipeableViewPager: swiping only works from the team tab, so the players
        // table can scroll sideways.
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = pagerState.currentPage == 0,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> TeamDataTab(state)
                else -> PlayersDataTab(state, onFilterSelected = viewModel::onQuarterFilterSelected)
            }
        }
    }
}

@Composable
private fun TeamDataTab(state: HistoryDetailUiState) {
    val textColor = colorResource(R.color.colorButtonInMain)
    val info = state.info ?: return
    val quarters = state.teamByQuarter
    val total = state.teamTotal

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            val win = HistoryStats.isWin(info.yourScore, info.opponentScore)
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(if (win) R.string.win else R.string.lose),
                    color = colorResource(if (win) R.color.colorRed else R.color.colorGreen),
                    fontSize = 35.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ScoreColumn("我方", info.yourScore, textColor)
                    Text("：", color = colorResource(R.color.colorRed), fontSize = 35.sp)
                    ScoreColumn("對方", info.opponentScore, textColor)
                }
            }
        }

        item {
            SectionTitle("命中率", textColor)
            Row(modifier = Modifier.fillMaxWidth()) {
                PercentagePieChart(
                    percent = HistoryStats.percent(total.fgm, total.fga),
                    centerTextRes = R.string.fieldGoalInPie,
                    modifier = Modifier.weight(1f).height(200.dp)
                )
                PercentagePieChart(
                    percent = HistoryStats.percent(total.tpm, total.tpa),
                    centerTextRes = R.string.threePointInPie,
                    modifier = Modifier.weight(1f).height(200.dp)
                )
            }
            SectionTitle("得分組成", textColor)
            ScoringBarChart(
                stacks = (quarters + total).map(HistoryStats::scoring),
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            SectionTitle("統計", textColor)
            TeamTableRow(type = "", values = listOf("1st", "2nd", "3rd", "4th", "TOL"), color = textColor, height = 30.dp)
        }

        items(HistoryStats.STAT_COLUMNS.indices.toList()) { statIndex ->
            val values = (quarters + total).map { HistoryStats.values(it)[statIndex].toString() }
            TeamTableRow(type = HistoryStats.STAT_COLUMNS[statIndex], values = values, color = textColor, height = 35.dp)
        }
    }
}

@Composable
private fun ScoreColumn(label: String, score: Int, labelColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(label, color = labelColor, fontSize = 15.sp)
        Text(score.toString(), color = colorResource(R.color.colorRed), fontSize = 60.sp)
    }
}

@Composable
private fun SectionTitle(text: String, color: Color) {
    Text(text, color = color, fontSize = 15.sp, modifier = Modifier.padding(16.dp))
}

@Composable
private fun TeamTableRow(type: String, values: List<String>, color: Color, height: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth().height(height).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableText(type, color)
        values.forEach { TableText(it, color) }
    }
}

@Composable
private fun RowScope.TableText(text: String, color: Color) {
    Text(text, color = color, fontSize = 15.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
}

/** Legacy table always shows at least 15 player rows. */
private const val MIN_PLAYER_ROWS = 15
private val NAME_WIDTH = 90.dp
private val CELL_WIDTH = 60.dp
private val CELL_HEIGHT = 40.dp

@Composable
private fun PlayersDataTab(state: HistoryDetailUiState, onFilterSelected: (Int) -> Unit) {
    var showFilter by remember { mutableStateOf(false) }
    val horizontal = rememberScrollState()
    val players: List<GameDataEntity?> = state.players.let { it + List((MIN_PLAYER_ROWS - it.size).coerceAtLeast(0)) { null } }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Row {
            StatCell(
                text = stringResource(QUARTER_FILTER_LABELS[state.quarterFilter]) + " ▾",
                width = NAME_WIDTH,
                highlighted = true,
                modifier = Modifier.clickable { showFilter = true }
            )
            ScrollingCells(HistoryStats.STAT_COLUMNS, horizontal)
        }
        players.forEach { player ->
            Row {
                StatCell(text = player?.playerName.orEmpty(), width = NAME_WIDTH)
                ScrollingCells(player?.let { HistoryStats.values(it).map(Int::toString) } ?: List(HistoryStats.STAT_COLUMNS.size) { "" }, horizontal)
            }
        }
    }

    if (showFilter) {
        AlertDialog(
            onDismissRequest = { showFilter = false },
            title = { Text(stringResource(R.string.quarterFilter)) },
            text = {
                Column {
                    QUARTER_FILTER_LABELS.forEachIndexed { index, label ->
                        TextButton(
                            onClick = { showFilter = false; onFilterSelected(index) },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(stringResource(label)) }
                    }
                }
            },
            buttons = {}
        )
    }
}

/** Every row shares one [ScrollState], so the stat columns scroll together. */
@Composable
private fun ScrollingCells(values: List<String>, scrollState: ScrollState) {
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
        values.forEach { StatCell(text = it, width = CELL_WIDTH) }
    }
}

@Composable
private fun StatCell(text: String, width: Dp, highlighted: Boolean = false, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(width)
            .height(CELL_HEIGHT)
            .padding(1.dp)
            .background(colorResource(if (highlighted) R.color.colorOrange else R.color.colorDarkBackground)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colorResource(R.color.colorButtonInMain),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

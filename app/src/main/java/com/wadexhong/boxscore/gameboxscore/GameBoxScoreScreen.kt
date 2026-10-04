package com.wadexhong.boxscore.gameboxscore

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.gameboxscore.datastatistic.DataStatisticDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameBoxScoreScreen(
    viewModel: GameBoxScoreViewModel,
    /** Back button flow: save = keep the game resumable, otherwise discard it. */
    onExit: (save: Boolean) -> Unit = {},
    /** Save icon flow: end the game and store the result. */
    onEndGame: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedStatType by remember { mutableStateOf<Int?>(null) }
    var selectedChangePlayer by remember { mutableStateOf<TeamPlayerEntity?>(null) }
    var showDataStatisticDialog by remember { mutableStateOf(false) }
    var showQuarterDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showEndGameDialog by remember { mutableStateOf(false) }
    var showMakeUpDialog by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Record", "Change Player", "Undo History")
    val allPlayers = state.onCourtPlayers + state.benchPlayers

    BackHandler {
        BoxScore.vibrate()
        showExitDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .multiFingerSwipe { fingerCount, direction ->
                BoxScore.vibrate()
                val type = viewModel.actionForGesture(SwipeGesture(fingerCount, direction))
                if (type != -1) selectedStatType = type
            }
    ) {
        ScoreboardSection(
            state = state,
            onMakeupClicked = {
                if (state.quarter != 1) showMakeUpDialog = true
                else Toast.makeText(context, "第一節不須補登", Toast.LENGTH_SHORT).show()
            },
            onOpponentScorePlus = { viewModel.onOpponentScoreChanged(1) },
            onOpponentScoreMinus = { viewModel.onOpponentScoreChanged(-1) },
            onOpponentFoulPlus = { viewModel.onOpponentFoulChanged(1) },
            onOpponentFoulMinus = { viewModel.onOpponentFoulChanged(-1) },
            onSaveClicked = { showEndGameDialog = true },
            onSettingsClicked = onOpenSettings,
            onTableClicked = { showDataStatisticDialog = true },
            onQuarterClicked = { showQuarterDialog = true },
            onYourFoulClicked = { viewModel.onYourFoulChanged(1) }
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = colorResource(id = R.color.secondaryDarkColor)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> DataRecordTab(
                    onStatClicked = { stat ->
                        val type = mapStatToInt(stat)
                        if (type != -1) selectedStatType = type
                    }
                )
                1 -> ChangePlayerTab(
                    onCourtPlayers = state.onCourtPlayers,
                    onPlayerClicked = { player -> selectedChangePlayer = player }
                )
                2 -> UndoHistoryTab(
                    actionHistory = state.actionHistory,
                    players = allPlayers,
                    onUndoAction = { id -> viewModel.onUndoAction(id) },
                    onEditAction = { event, playerId, type -> viewModel.onEditEvent(event, playerId, type) }
                )
            }
        }
    }

    // Dialog for Data Record Stat Selection (from a stat button or a multi-finger swipe)
    selectedStatType?.let { type ->
        PlayerSelectDialog(
            players = state.onCourtPlayers,
            onDismissRequest = { selectedStatType = null },
            onPlayerSelected = { player ->
                viewModel.onStatRecorded(type, player.playerId)
                selectedStatType = null
            }
        )
    }

    // Dialog for Change Player Selection
    selectedChangePlayer?.let { courtPlayer ->
        ChangePlayerDialog(
            benchPlayers = state.benchPlayers,
            onDismissRequest = { selectedChangePlayer = null },
            onPlayerSelected = { benchPlayer ->
                viewModel.onPlayerSubstituted(courtPlayer.playerId, benchPlayer.playerId)
                selectedChangePlayer = null
            }
        )
    }

    if (showDataStatisticDialog) {
        DataStatisticDialog(
            players = allPlayers,
            onDismiss = { showDataStatisticDialog = false }
        )
    }

    if (showMakeUpDialog) {
        MakeUpDialog(
            currentQuarter = state.quarter,
            players = allPlayers,
            onDismiss = { showMakeUpDialog = false },
            onConfirm = { playerId, type, quarter ->
                viewModel.onMakeUp(playerId, type, quarter)
                showMakeUpDialog = false
            }
        )
    }

    if (showQuarterDialog) {
        AlertDialog(
            onDismissRequest = { showQuarterDialog = false },
            title = { Text(stringResource(R.string.ask_next_quarter_title)) },
            text = { Text(stringResource(R.string.ask_next_quarter_message)) },
            confirmButton = {
                TextButton(onClick = {
                    BoxScore.vibrate()
                    viewModel.advanceQuarter()
                    showQuarterDialog = false
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuarterDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showEndGameDialog) {
        AlertDialog(
            onDismissRequest = { showEndGameDialog = false },
            title = { Text(stringResource(R.string.ask_save_title)) },
            text = { Text(stringResource(R.string.ask_save_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showEndGameDialog = false
                    onEndGame()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndGameDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.confirmExitDataRecord)) },
            text = { Text(stringResource(R.string.exitDataRecordMessage)) },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onExit(true)
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        showExitDialog = false
                        onExit(false)
                    }) {
                        Text(stringResource(R.string.discardGame))
                    }
                }
            }
        )
    }
}

private fun mapStatToInt(stat: String): Int {
    return when (stat) {
        "2PM" -> Constants.RecordDataType.TWO_POINT_SHOT_MADE
        "2PA" -> Constants.RecordDataType.TWO_POINT_SHOT_MISSED
        "3PM" -> Constants.RecordDataType.THREE_POINT_SHOT_MADE
        "3PA" -> Constants.RecordDataType.THREE_POINT_SHOT_MISSED
        "FTM" -> Constants.RecordDataType.FREE_THROW_SHOT_MADE
        "FTA" -> Constants.RecordDataType.FREE_THROW_SHOT_MISSED
        "OREB" -> Constants.RecordDataType.OFFENSIVE_REBOUND
        "DREB" -> Constants.RecordDataType.DEFENSIVE_REBOUND
        "AST" -> Constants.RecordDataType.ASSIST
        "STL" -> Constants.RecordDataType.STEAL
        "BLK" -> Constants.RecordDataType.BLOCK
        "TO" -> Constants.RecordDataType.TURNOVER
        "PF" -> Constants.RecordDataType.FOUL
        else -> -1
    }
}

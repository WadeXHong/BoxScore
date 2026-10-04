package com.wadexhong.boxscore.startgame

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.wadexhong.boxscore.R
import kotlinx.coroutines.launch

@Composable
fun StartGameScreen(
    viewModel: StartGameViewModel,
    /** Leaves the wizard; only called after the user confirmed. */
    onBack: () -> Unit,
    onStartGame: (com.wadexhong.boxscore.objects.GameInfo) -> Unit,
    onNoTeams: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showBackConfirm by remember { mutableStateOf(false) }
    var showInputIllegal by remember { mutableStateOf(false) }

    /** Moves to [target] if every page before it is valid; otherwise shows the legacy dialog. */
    fun navigateTo(target: Int) {
        if (target > pagerState.currentPage && viewModel.firstInvalidPageBefore(target) != -1) {
            showInputIllegal = true
        } else {
            coroutineScope.launch { pagerState.animateScrollToPage(target) }
        }
    }

    BackHandler { showBackConfirm = true }

    LaunchedEffect(Unit) {
        viewModel.limitMessages.collect { message ->
            Toast.makeText(context, context.getString(message.messageRes, message.limit), Toast.LENGTH_SHORT).show()
        }
    }

    // Swiping is allowed like the legacy ViewPager, but landing on a page re-checks every
    // earlier page and bounces back to the first invalid one.
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            val invalidPage = viewModel.firstInvalidPageBefore(page)
            if (invalidPage != -1) {
                showInputIllegal = true
                pagerState.animateScrollToPage(invalidPage)
            }
        }
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (pagerState.currentPage) {
                            0 -> "Game Name Setting"
                            1 -> "Player List"
                            else -> "Detail Setting"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (pagerState.currentPage > 0) {
                            navigateTo(pagerState.currentPage - 1)
                        } else {
                            showBackConfirm = true
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (pagerState.currentPage < 2) {
                        IconButton(onClick = { navigateTo(pagerState.currentPage + 1) }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                        }
                    } else {
                        IconButton(onClick = {
                            if (viewModel.firstInvalidPageBefore(pagerState.pageCount) != -1) {
                                showInputIllegal = true
                            } else {
                                onStartGame(viewModel.buildGameInfo())
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Start Game")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val tabs = listOf("Game Name", "Player List", "Detail")
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { if (index != pagerState.currentPage) navigateTo(index) },
                        text = { Text(title) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> GameNameSettingPage(viewModel)
                    1 -> PlayerListPage(viewModel)
                    2 -> DetailSettingPage(viewModel)
                }
            }
        }
    }

    if (showInputIllegal) {
        AlertDialog(
            onDismissRequest = { showInputIllegal = false },
            title = { Text(stringResource(R.string.start_game_illegal_title)) },
            text = { Text(stringResource(R.string.start_game_illegal_message)) },
            confirmButton = {
                TextButton(onClick = { showInputIllegal = false }) {
                    Text(stringResource(R.string.confirm))
                }
            }
        )
    }

    if (showBackConfirm) {
        AlertDialog(
            onDismissRequest = { showBackConfirm = false },
            title = { Text(stringResource(R.string.confirmGoBack)) },
            text = { Text(stringResource(R.string.goBackConfirmMessage)) },
            confirmButton = {
                TextButton(onClick = {
                    showBackConfirm = false
                    onBack()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showBackConfirm = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (!uiState.isLoading && uiState.teams.isEmpty()) {
        AlertDialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { Text(stringResource(R.string.no_legal_team_title)) },
            text = { Text(stringResource(R.string.no_legal_team_message)) },
            confirmButton = {
                TextButton(onClick = onNoTeams) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

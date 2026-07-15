package com.wadexhong.boxscore.startgame

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun StartGameScreen(
    viewModel: StartGameViewModel,
    onBack: () -> Unit,
    onStartGame: (com.wadexhong.boxscore.objects.GameInfo) -> Unit,
    onNoTeams: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.teams, uiState.isLoading) {
        if (!uiState.isLoading && uiState.teams.isEmpty()) {
            onNoTeams()
        }
    }

    Scaffold(
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
                            coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (pagerState.currentPage < 2) {
                        IconButton(onClick = {
                            val isValid = when (pagerState.currentPage) {
                                0 -> viewModel.validateStep1()
                                1 -> viewModel.validateStep2()
                                else -> true
                            }
                            if (isValid) {
                                coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            }
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                        }
                    } else {
                        IconButton(onClick = {
                            val info = viewModel.buildGameInfo()
                            onStartGame(info)
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
                        onClick = {
                            if (index < pagerState.currentPage) {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            } else if (index > pagerState.currentPage) {
                                // Validate all steps between current and target
                                var canAdvance = true
                                for (i in pagerState.currentPage until index) {
                                    val isValid = when (i) {
                                        0 -> viewModel.validateStep1()
                                        1 -> viewModel.validateStep2()
                                        else -> true
                                    }
                                    if (!isValid) {
                                        canAdvance = false
                                        break
                                    }
                                }
                                if (canAdvance) {
                                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                }
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }
            
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> GameNameSettingPage(viewModel)
                    1 -> PlayerListPage(viewModel)
                    2 -> DetailSettingPage(viewModel)
                }
            }
        }
    }
}

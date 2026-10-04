package com.wadexhong.boxscore.gameboxscore

import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import com.wadexhong.boxscore.testutil.FakeGameStore
import com.wadexhong.boxscore.testutil.FakeTeamStore
import com.wadexhong.boxscore.testutil.MainDispatcherRule
import com.wadexhong.boxscore.testutil.team
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GameBoxScoreViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val games = FakeGameStore()
    private val teams = FakeTeamStore()
    private val repo = GameRepository(games.gameDao, games.eventDao, teams.teamDao)

    private fun gameInfo(maxFoul: Int = 5, totalQuarter: Int = 4) = GameInfo().apply {
        gameId = "g1"
        yourTeamId = "t1"
        yourTeam = "Lakers"
        opponentName = "Bulls"
        this.maxFoul = maxFoul.toString()
        this.totalQuarter = totalQuarter.toString()
        startingPlayerList = ArrayList((1..5).map { Player("$it", "P$it", "p$it") })
        substitutePlayerList = ArrayList(listOf(Player("6", "P6", "p6"), Player("7", "P7", "p7")))
    }

    private fun savedInfo() = games.infos.value.find { it.gameId == "g1" }

    private fun viewModel(info: GameInfo = gameInfo()) =
        GameBoxScoreViewModel(games.eventDao, teams.playerDao, repo, info.gameId, info.yourTeamId, info)

    @Test
    fun newGame_isSavedWithLineupFromStartGame() {
        val vm = viewModel()
        val state = vm.uiState.value

        assertNotNull(savedInfo())
        assertEquals((1..5).map { "p$it" }, state.onCourtPlayers.map { it.playerId })
        assertEquals(listOf("p6", "p7"), state.benchPlayers.map { it.playerId })
        assertTrue(state.actionHistory.isEmpty())
    }

    @Test
    fun recordingStats_updatesScoreAndHistory() {
        val vm = viewModel()
        vm.onStatRecorded(RecordDataType.THREE_POINT_SHOT_MADE, "p1")
        vm.onStatRecorded(RecordDataType.ASSIST, "p2")
        vm.onOpponentScoreChanged(1)

        val state = vm.uiState.value
        assertEquals(3, state.yourScore)
        assertEquals(1, state.opponentScore)
        assertEquals(listOf(RecordDataType.THREE_POINT_SHOT_MADE, RecordDataType.ASSIST), state.actionHistory.map { it.actionType })
        assertEquals(3, state.playerTotals.single { it.playerId == "p1" }.pts)
    }

    @Test
    fun undoAndEdit_changeTheRecordedEvent() {
        val vm = viewModel()
        vm.onStatRecorded(RecordDataType.TWO_POINT_SHOT_MADE, "p1")
        val event = vm.uiState.value.actionHistory.single()

        vm.onEditEvent(event, "p2", RecordDataType.THREE_POINT_SHOT_MADE)
        assertEquals(3, vm.uiState.value.yourScore)
        assertEquals("p2", vm.uiState.value.actionHistory.single().playerId)

        vm.onUndoAction(event.id)
        assertEquals(0, vm.uiState.value.yourScore)
        assertTrue(vm.uiState.value.actionHistory.isEmpty())
    }

    @Test
    fun teamFouls_stopAtMaxAndNeverGoNegative() {
        val vm = viewModel(gameInfo(maxFoul = 2))
        repeat(3) { vm.onOpponentFoulChanged(1) }
        assertEquals(2, vm.uiState.value.opponentFouls)

        repeat(3) { vm.onOpponentFoulChanged(-1) }
        assertEquals(0, vm.uiState.value.opponentFouls)

        vm.onOpponentScoreChanged(-1)
        assertEquals(0, vm.uiState.value.opponentScore)
    }

    @Test
    fun quarter_stopsAtTotalQuarters() {
        val vm = viewModel(gameInfo(totalQuarter = 2))
        repeat(3) { vm.advanceQuarter() }
        assertEquals(2, vm.uiState.value.quarter)
    }

    @Test
    fun substitution_swapsCourtAndBench_andIgnoresInvalidPairs() {
        val vm = viewModel()
        vm.onPlayerSubstituted("p3", "p6")
        assertEquals(listOf("p1", "p2", "p6", "p4", "p5"), vm.uiState.value.onCourtPlayers.map { it.playerId })
        assertEquals(listOf("p3", "p7"), vm.uiState.value.benchPlayers.map { it.playerId })

        // p7 is on the bench, not on court: ignored.
        vm.onPlayerSubstituted("p7", "p3")
        assertEquals(listOf("p1", "p2", "p6", "p4", "p5"), vm.uiState.value.onCourtPlayers.map { it.playerId })
    }

    @Test
    fun makeUp_recordsInEarlierQuarterWithoutTouchingCurrentTeamFouls() {
        val vm = viewModel()
        vm.advanceQuarter()
        vm.onMakeUp("p1", RecordDataType.DEFENSIVE_FOUL, quarter = 1)

        assertEquals(0, vm.uiState.value.yourFouls)
        assertEquals(1, vm.uiState.value.playerTotals.single { it.playerId == "p1" }.pf)
    }

    @Test
    fun finishGame_storesResultAndHistoryCount() = runTest {
        teams.teamDao.insertTeam(team("t1"))
        val vm = viewModel()
        vm.onStatRecorded(RecordDataType.FREE_THROW_SHOT_MADE, "p1")

        var finished: Int? = null
        vm.finishGame { finished = it?.yourScore }

        assertEquals(1, finished)
        assertEquals(1, savedInfo()!!.isGameover)
        assertEquals(1, teams.teamDao.getTeamById("t1")!!.historyAmount)
    }

    @Test
    fun discardGame_removesTheGame() {
        val vm = viewModel()
        vm.onStatRecorded(RecordDataType.STEAL, "p1")
        var done = false
        vm.discardGame { done = true }

        assertTrue(done)
        assertNull(savedInfo())
        assertTrue(games.events.value.isEmpty())
    }

    @Test
    fun resumedGame_restoresStateFromEvents() {
        viewModel().apply {
            onStatRecorded(RecordDataType.TWO_POINT_SHOT_MADE, "p1")
            onPlayerSubstituted("p1", "p7")
            advanceQuarter()
        }

        val resumed = viewModel()
        val state = resumed.uiState.value
        assertEquals(2, state.yourScore)
        assertEquals(2, state.quarter)
        assertTrue(state.onCourtPlayers.any { it.playerId == "p7" })
        assertEquals(1, games.infos.value.size)
    }
}

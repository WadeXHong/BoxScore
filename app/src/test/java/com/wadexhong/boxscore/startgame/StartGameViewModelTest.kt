package com.wadexhong.boxscore.startgame

import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.testutil.FakeTeamStore
import com.wadexhong.boxscore.testutil.MainDispatcherRule
import com.wadexhong.boxscore.testutil.player
import com.wadexhong.boxscore.testutil.team
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StartGameViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val store = FakeTeamStore()

    private fun seedTeam(id: String, playerCount: Int) {
        store.teams.value = store.teams.value + team(id)
        store.players.value = store.players.value + (1..playerCount).map { player(id, "$id-$it", "$it") }
    }

    private fun viewModel() = StartGameViewModel(store.teamDao, store.playerDao)

    @Test
    fun onlyTeamsWithSixPlayersAreOffered() {
        seedTeam("small", 5)
        seedTeam("ok", 6)
        val vm = viewModel()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(listOf("ok"), state.teams.map { it.teamId })
        assertEquals("ok", state.selectedTeam?.teamId)
        assertEquals(6, state.players.size)
    }

    @Test
    fun noLegalTeam_leavesSelectionEmpty() {
        seedTeam("small", 3)
        val state = viewModel().uiState.value
        assertTrue(state.teams.isEmpty())
        assertNull(state.selectedTeam)
    }

    @Test
    fun selectedTeamKeepsItsPlayersWhenTeamListChanges() {
        seedTeam("a", 6)
        seedTeam("b", 7)
        val vm = viewModel()
        vm.onTeamSelected(store.teams.value.single { it.teamId == "b" })

        // Another team appearing re-emits the team list; the roster must stay on team b.
        seedTeam("c", 6)

        assertEquals("b", vm.uiState.value.selectedTeam?.teamId)
        assertTrue(vm.uiState.value.players.all { it.teamId == "b" })
        assertEquals(7, vm.uiState.value.players.size)
    }

    @Test
    fun selectingTeam_clearsLineup() {
        seedTeam("a", 6)
        seedTeam("b", 6)
        val vm = viewModel()
        vm.moveToStarting("a-1")
        vm.onTeamSelected(store.teams.value.single { it.teamId == "b" })
        assertTrue(vm.uiState.value.startingPlayerIds.isEmpty())
    }

    @Test
    fun step1_requiresOpponentName() {
        seedTeam("a", 6)
        val vm = viewModel()
        assertFalse(vm.validatePage(0))
        assertTrue(vm.uiState.value.isStep1Error)

        vm.onOpponentNameChanged("   ")
        assertEquals("", vm.uiState.value.opponentName)
        assertFalse(vm.validatePage(0))

        vm.onOpponentNameChanged("  Bulls")
        assertEquals("Bulls", vm.uiState.value.opponentName)
        assertTrue(vm.validatePage(0))
    }

    @Test
    fun textInputs_areCappedAtTwentyCharacters() {
        seedTeam("a", 6)
        val vm = viewModel()
        vm.onGameTitleChanged("x".repeat(20))
        vm.onGameTitleChanged("x".repeat(21))
        assertEquals(20, vm.uiState.value.gameTitle.length)
    }

    @Test
    fun step2_requiresFiveStartersAndOneSubstitute() {
        seedTeam("a", 7)
        val vm = viewModel()
        (1..5).forEach { vm.moveToStarting("a-$it") }
        assertFalse(vm.validatePage(1))

        vm.moveToStarting("a-6")
        assertEquals(5, vm.uiState.value.startingPlayerIds.size)

        vm.moveToSubstitute("a-6")
        assertTrue(vm.validatePage(1))
    }

    @Test
    fun firstInvalidPageBefore_returnsEarliestInvalidPage() {
        seedTeam("a", 6)
        val vm = viewModel()
        assertEquals(0, vm.firstInvalidPageBefore(2))

        vm.onOpponentNameChanged("Bulls")
        assertEquals(1, vm.firstInvalidPageBefore(2))
        assertEquals(-1, vm.firstInvalidPageBefore(1))

        (1..5).forEach { vm.moveToStarting("a-$it") }
        vm.moveToSubstitute("a-6")
        assertEquals(-1, vm.firstInvalidPageBefore(3))
    }

    @Test
    fun steppers_stayWithinLegacyBoundsAndEmitToast() = runTest {
        seedTeam("a", 6)
        val vm = viewModel()
        val messages = mutableListOf<LimitMessage>()
        val job = launch(mainRule.dispatcher) { vm.limitMessages.collect { messages += it } }

        vm.onTotalQuartersChanged(5)
        vm.onMaxFoulsChanged(0)
        vm.onTimeoutsFirstHalfChanged(10)
        vm.onQuarterLengthChanged(41)
        vm.onQuarterLengthChanged(40)

        val state = vm.uiState.value
        assertEquals(4, state.totalQuarters)
        assertEquals(5, state.maxFouls)
        assertEquals(5, state.timeoutsFirstHalf)
        assertEquals(40, state.quarterLength)
        assertEquals(
            listOf(
                LimitMessage(R.string.max_total_quarter_message, 4),
                LimitMessage(R.string.min_foul_message, 1),
                LimitMessage(R.string.max_timeout_first_half, 9),
                LimitMessage(R.string.max_quarter_length_message, 40)
            ),
            messages
        )
        job.cancel()
    }

    @Test
    fun buildGameInfo_carriesLineupAndSettings() {
        seedTeam("a", 7)
        val vm = viewModel()
        vm.onOpponentNameChanged("Bulls")
        (1..5).forEach { vm.moveToStarting("a-$it") }
        vm.moveToSubstitute("a-7")
        vm.onTotalQuartersChanged(2)

        val info = vm.buildGameInfo()
        assertTrue(info.gameId.isNotEmpty())
        assertEquals("a", info.yourTeamId)
        assertEquals("Bulls", info.opponentName)
        assertEquals("2", info.totalQuarter)
        assertEquals((1..5).map { "a-$it" }, info.startingPlayerList.map { it.playerId })
        assertEquals(listOf("a-7"), info.substitutePlayerList.map { it.playerId })
    }
}

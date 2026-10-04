package com.wadexhong.boxscore.gamehistory

import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import com.wadexhong.boxscore.testutil.FakeGameStore
import com.wadexhong.boxscore.testutil.FakeTeamStore
import com.wadexhong.boxscore.testutil.MainDispatcherRule
import com.wadexhong.boxscore.testutil.team
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class GameHistoryTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val games = FakeGameStore()
    private val teams = FakeTeamStore()
    private val repo = GameRepository(games.gameDao, games.eventDao, teams.teamDao)

    private fun row(playerId: String, number: String, quarter: Int, pts: Int = 0, fgm: Int = 0, fga: Int = 0, tpm: Int = 0, tpa: Int = 0, ftm: Int = 0) =
        GameDataEntity(
            gameId = "g", quarter = quarter, playerId = playerId, playerNumber = number, playerName = "P$number",
            pts = pts, fgm = fgm, fga = fga, tpm = tpm, tpa = tpa, ftm = ftm, fta = 0,
            oreb = 0, dreb = 0, ast = 0, stl = 0, blk = 0, pf = 0, tov = 0
        )

    private suspend fun playFinishedGame(id: String, date: String, threes: Int = 1) {
        val info = GameInfo().apply {
            gameId = id
            yourTeamId = "t1"
            yourTeam = "Lakers"
            opponentName = "Bulls"
            gameDate = date
            totalQuarter = "4"
            startingPlayerList = ArrayList((1..5).map { Player("$it", "P$it", "p$it") })
            substitutePlayerList = ArrayList(listOf(Player("6", "P6", "p6")))
        }
        repo.startGameIfNew(info)
        repeat(threes) {
            games.eventDao.insertEvent(GameEventEntity(gameId = id, actionType = RecordDataType.THREE_POINT_SHOT_MADE, playerId = "p1", quarter = 1))
        }
        repo.finishGame(id)
    }

    @Test
    fun teamByQuarter_sumsPlayersPerQuarter() {
        val rows = listOf(
            row("a", "1", 1, pts = 2), row("b", "2", 1, pts = 3),
            row("a", "1", 3, pts = 4)
        )
        assertEquals(listOf(5, 0, 4, 0), HistoryStats.teamByQuarter(rows).map { it.pts })
        assertEquals(9, HistoryStats.sum(rows).pts)
    }

    @Test
    fun playersFor_filtersQuarterAndSortsByNumber() {
        val rows = listOf(
            row("ten", "10", 1, pts = 1), row("two", "2", 1, pts = 5),
            row("ten", "10", 2, pts = 3), row("two", "2", 2, pts = 0)
        )
        val total = HistoryStats.playersFor(rows, HistoryStats.FILTER_TOTAL)
        assertEquals(listOf("2", "10"), total.map { it.playerNumber })
        assertEquals(listOf(5, 4), total.map { it.pts })

        assertEquals(listOf(0, 3), HistoryStats.playersFor(rows, 2).map { it.pts })
    }

    @Test
    fun scoringAndPercentages() {
        val r = row("a", "1", 1, fgm = 3, fga = 6, tpm = 1, tpa = 4, ftm = 2)
        assertEquals(Triple(2, 4, 3), HistoryStats.scoring(r))
        assertEquals(50f, HistoryStats.percent(r.fgm, r.fga))
        assertEquals(0f, HistoryStats.percent(0, 0))
        assertTrue(HistoryStats.isWin(70, 70))
    }

    @Test
    fun listShowsFinishedGamesNewestFirst() = runTest {
        playFinishedGame("old", "2026年01月01日")
        playFinishedGame("new", "2026年02月01日")
        repo.startGameIfNew(GameInfo().apply { gameId = "playing"; yourTeamId = "t1" })

        val vm = listViewModel()
        assertEquals(listOf("new", "old"), vm.games.value.map { it.gameId })
    }

    @Test
    fun deleteGame_removesItAndReportsNewCount() = runTest {
        teams.teamDao.insertTeam(team("t1"))
        playFinishedGame("g1", "2026年01月01日")
        playFinishedGame("g2", "2026年01月02日")
        var reported: Triple<String, String, Int>? = null
        val vm = listViewModel(onDeleted = { t, g, n -> reported = Triple(t, g, n) })

        vm.deleteGame(vm.games.value.single { it.gameId == "g1" })

        assertEquals(listOf("g2"), vm.games.value.map { it.gameId })
        assertEquals(Triple("t1", "g1", 1), reported)
        assertEquals(1, teams.teamDao.getTeamById("t1")!!.historyAmount)
    }

    @Test
    fun shareGame_exportsUploadsAndEmitsUrl() = runTest {
        playFinishedGame("g1", "2026年01月01日")
        var uploaded: File? = null
        val vm = listViewModel(uploader = ExcelUploader { _, file -> uploaded = file; "https://example/g1.xls" })
        val events = mutableListOf<HistoryEvent>()
        val job = launch(mainRule.dispatcher) { vm.events.collect { events += it } }

        vm.shareGame(vm.games.value.single())

        assertEquals(listOf<HistoryEvent>(HistoryEvent.Shared("https://example/g1.xls")), events)
        assertTrue(uploaded!!.exists() && uploaded!!.name == "g1.xls")
        assertEquals(false, vm.isUploading.value)
        job.cancel()
    }

    @Test
    fun shareGame_reportsFailures() = runTest {
        playFinishedGame("g1", "2026年01月01日")
        val vm = listViewModel(uploader = ExcelUploader { _, _ -> null })
        val events = mutableListOf<HistoryEvent>()
        val job = launch(mainRule.dispatcher) { vm.events.collect { events += it } }

        vm.shareGame(vm.games.value.single())
        // A game without stat rows cannot be exported (legacy onError).
        games.gameDao.deleteGameDataByGameId("g1")
        vm.shareGame(vm.games.value.single())

        assertEquals(listOf(HistoryEvent.ShareFailed, HistoryEvent.CannotCreateExcel), events)
        job.cancel()
    }

    @Test
    fun detail_loadsGameAndAppliesQuarterFilter() = runTest {
        playFinishedGame("g1", "2026年01月01日", threes = 2)
        val vm = HistoryDetailViewModel(games.gameDao, "g1")

        val state = vm.uiState.value
        assertEquals("g1", state.info?.gameId)
        assertEquals(6, state.teamByQuarter.first().pts)
        assertEquals(6, state.teamTotal.pts)
        assertEquals(6, state.players.size)
        assertEquals(6, state.players.first { it.playerId == "p1" }.pts)

        vm.onQuarterFilterSelected(2)
        assertEquals(0, vm.uiState.value.players.first { it.playerId == "p1" }.pts)
    }

    private fun listViewModel(
        uploader: ExcelUploader = ExcelUploader { _, _ -> null },
        onDeleted: (String, String, Int) -> Unit = { _, _, _ -> }
    ) = GameHistoryListViewModel(games.gameDao, repo, tempFolder.newFolder(), uploader, onDeleted, mainRule.dispatcher)
}

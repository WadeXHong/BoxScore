package com.wadexhong.boxscore.database

import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import com.wadexhong.boxscore.testutil.FakeGameStore
import com.wadexhong.boxscore.testutil.FakeTeamStore
import com.wadexhong.boxscore.testutil.team
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRepositoryTest {

    private val games = FakeGameStore()
    private val teams = FakeTeamStore()
    private val repo = GameRepository(games.gameDao, games.eventDao, teams.teamDao)

    private fun gameInfo(id: String = "g1", teamId: String = "t1", players: Int = 6) = GameInfo().apply {
        gameId = id
        gameName = "Friendly"
        yourTeamId = teamId
        yourTeam = "Team $teamId"
        opponentName = "Bulls"
        quarterLength = "10"
        totalQuarter = "4"
        maxFoul = "5"
        timeoutFirstHalf = "2"
        timeoutSecondHalf = "3"
        gameDate = "2026年10月04日"
        val all = (1..players).map { Player("$it", "P$it", "p$it") }
        startingPlayerList = ArrayList(all.take(5))
        substitutePlayerList = ArrayList(all.drop(5))
    }

    private suspend fun record(type: Int, playerId: String? = null, quarter: Int = 1, gameId: String = "g1") {
        games.eventDao.insertEvent(GameEventEntity(gameId = gameId, actionType = type, playerId = playerId, quarter = quarter))
    }

    @Test
    fun startGame_writesInfoAndZeroedRowsStartersFirst() = runTest {
        repo.startGameIfNew(gameInfo())

        val info = games.gameDao.getGameInfoById("g1")!!
        assertEquals(0, info.isGameover)
        assertEquals(5, info.maxFoul)
        assertEquals("Bulls", info.opponentName)

        val rows = games.gameDao.getGameDataList("g1")
        assertEquals(6 * 4, rows.size)
        assertEquals((1..6).map { "p$it" }, rows.map { it.playerId }.distinct())
        assertTrue(rows.all { it.pts == 0 && it.pf == 0 })
    }

    @Test
    fun startGame_isIdempotent() = runTest {
        repo.startGameIfNew(gameInfo())
        repo.startGameIfNew(gameInfo())
        assertEquals(1, games.infos.value.size)
        assertEquals(24, games.data.value.size)
    }

    @Test
    fun loadGameInfo_restoresSettingsAndLineup() = runTest {
        repo.startGameIfNew(gameInfo(players = 8))

        val loaded = repo.loadGameInfo("g1")!!
        assertEquals("t1", loaded.yourTeamId)
        assertEquals("4", loaded.totalQuarter)
        assertEquals("5", loaded.maxFoul)
        assertEquals((1..5).map { "p$it" }, loaded.startingPlayerList.map { it.playerId })
        assertEquals(listOf("p6", "p7", "p8"), loaded.substitutePlayerList.map { it.playerId })
        assertNull(repo.loadGameInfo("missing"))
    }

    @Test
    fun unfinishedGame_isTheOpenOne() = runTest {
        repo.startGameIfNew(gameInfo("old"))
        repo.finishGame("old")
        assertNull(repo.getUnfinishedGame())

        repo.startGameIfNew(gameInfo("new"))
        assertEquals("new", repo.getUnfinishedGame()?.gameId)
    }

    @Test
    fun saveProgress_aggregatesEventsIntoRowsAndScore() = runTest {
        repo.startGameIfNew(gameInfo())
        record(RecordDataType.THREE_POINT_SHOT_MADE, "p1")
        record(RecordDataType.QUARTER)
        record(RecordDataType.TWO_POINT_SHOT_MADE, "p6", quarter = 2)
        record(RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, quarter = 2)

        val info = repo.saveProgress("g1")!!
        assertEquals(5, info.yourScore)
        assertEquals(1, info.opponentScore)
        assertEquals(0, info.isGameover)

        val rows = games.gameDao.getGameDataList("g1")
        assertEquals(24, rows.size)
        assertEquals(3, rows.row("p1", 1).pts)
        assertEquals(2, rows.row("p6", 2).pts)
        assertEquals(0, rows.row("p1", 2).pts)
        // Roster order survives the rewrite, so a resume still finds the starters first.
        assertEquals((1..6).map { "p$it" }, rows.map { it.playerId }.distinct())
    }

    @Test
    fun saveProgress_keepsLegacyRowsWhenThereAreNoEvents() = runTest {
        repo.startGameIfNew(gameInfo())
        val legacyRow = games.gameDao.getGameDataList("g1").first()
        games.gameDao.updateGameData(legacyRow.copy(pts = 12))

        repo.saveProgress("g1")
        assertEquals(12, games.gameDao.getGameDataList("g1").first().pts)
    }

    @Test
    fun finishGame_marksOverAndUpdatesHistoryAmount() = runTest {
        teams.teamDao.insertTeam(team("t1"))
        repo.startGameIfNew(gameInfo("g1"))
        repo.finishGame("g1")
        repo.startGameIfNew(gameInfo("g2"))
        record(RecordDataType.FREE_THROW_SHOT_MADE, "p2", gameId = "g2")

        val finished = repo.finishGame("g2")!!
        assertEquals(1, finished.isGameover)
        assertEquals(1, finished.yourScore)
        assertEquals(2, teams.teamDao.getTeamById("t1")!!.historyAmount)
        assertNull(repo.finishGame("missing"))
    }

    @Test
    fun discardGame_removesEverything() = runTest {
        repo.startGameIfNew(gameInfo())
        record(RecordDataType.ASSIST, "p1")
        repo.discardGame("g1")

        assertTrue(games.infos.value.isEmpty())
        assertTrue(games.data.value.isEmpty())
        assertTrue(games.events.value.isEmpty())
    }

    @Test
    fun deleteFinishedGame_returnsNewHistoryAmount() = runTest {
        teams.teamDao.insertTeam(team("t1"))
        repo.startGameIfNew(gameInfo("g1"))
        repo.finishGame("g1")
        repo.startGameIfNew(gameInfo("g2"))
        repo.finishGame("g2")

        assertEquals(1, repo.deleteFinishedGame("g1", "t1"))
        assertEquals(1, teams.teamDao.getTeamById("t1")!!.historyAmount)
        assertNotNull(games.gameDao.getGameInfoById("g2"))
    }

    private fun List<GameDataEntity>.row(playerId: String, quarter: Int) =
        single { it.playerId == playerId && it.quarter == quarter }
}

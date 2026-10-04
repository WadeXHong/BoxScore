package com.wadexhong.boxscore.sync

import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.testutil.FakeGameStore
import com.wadexhong.boxscore.testutil.FakeTeamStore
import com.wadexhong.boxscore.testutil.player
import com.wadexhong.boxscore.testutil.team
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirebaseSyncTest {

    private fun info(id: String, over: Int = 1) = GameInfoEntity(
        gameId = id, gameName = "Cup", yourTeamId = "t1", yourTeamName = "Lakers", opponentName = "Bulls",
        quarterLength = 10, totalQuarter = 4, maxFoul = 5, timeoutFirstHalf = 2, timeoutSecondHalf = 3,
        yourScore = 7, opponentScore = 5, gameDate = "2026年01月01日", isGameover = over
    )

    private fun row(gameId: String, playerId: String, quarter: Int, pts: Int) = GameDataEntity(
        gameId = gameId, quarter = quarter, playerId = playerId, playerNumber = "23", playerName = "LeBron",
        pts = pts, fgm = 1, fga = 2, tpm = 0, tpa = 1, ftm = 3, fta = 4, oreb = 5, dreb = 6,
        ast = 7, stl = 8, blk = 9, pf = 1, tov = 2
    )

    /** Rebuilds the nested node a multi-path update produces, like the server would. */
    private fun nest(updates: Map<String, Any>): Map<String, Any> {
        val root = mutableMapOf<String, Any>()
        updates.forEach { (path, value) ->
            val keys = path.split("/")
            var node = root
            keys.dropLast(1).forEach { key ->
                @Suppress("UNCHECKED_CAST")
                node = node.getOrPut(key) { mutableMapOf<String, Any>() } as MutableMap<String, Any>
            }
            node[keys.last()] = value
        }
        return root
    }

    @Test
    fun gameUpload_usesLegacyLayout() {
        val updates = FirebaseMapper.gameUpload(info("g1"), listOf(row("g1", "p1", 2, pts = 9)), historyAmount = 4)

        assertEquals("Bulls", updates["game_info/g1/opponent"])
        assertEquals(1, updates["game_info/g1/is_gameover"])
        assertEquals("LeBron", updates["game_info/g1/game_data/p1/player_name"])
        assertEquals(9, updates["game_info/g1/game_data/p1/quarter/2/PTS"])
        assertEquals(2, updates["game_info/g1/game_data/p1/quarter/2/TOV"])
        assertEquals(4, updates["team_info/t1/history_amount"])
    }

    @Test
    fun parseUser_roundTripsUploadedGame() {
        val rows = (1..4).map { row("g1", "p1", it, pts = it) }
        val remote = FirebaseMapper.parseUser(nest(FirebaseMapper.gameUpload(info("g1"), rows, 1)))

        val game = remote.games.single()
        assertEquals(info("g1"), game.info)
        assertEquals(rows, game.rows)
    }

    @Test
    fun parseUser_acceptsQuartersReturnedAsList() {
        // Firebase turns the keys "1".."4" into a list with a null at index 0.
        val quarter = mapOf("PTS" to 3L, "FGM" to 1L)
        val value = mapOf(
            "game_info" to mapOf(
                "g1" to mapOf(
                    "is_gameover" to 1L,
                    "game_data" to mapOf(
                        "p1" to mapOf("player_name" to "A", "player_number" to "1", "quarter" to listOf(null, quarter, quarter))
                    )
                )
            )
        )
        val rows = FirebaseMapper.parseUser(value).games.single().rows
        assertEquals(listOf(1, 2), rows.map { it.quarter })
        assertEquals(listOf(3, 3), rows.map { it.pts })
    }

    @Test
    fun parseUser_readsTeamsAndPlayers() {
        val team = team("t1").copy(playersAmount = 6, historyAmount = 2)
        val player = player("t1", "p1", "8").copy(center = 1)
        val value = mapOf(
            "team_info" to mapOf("t1" to FirebaseMapper.teamValues(team)),
            "team_player" to mapOf("p1" to FirebaseMapper.playerValues(player))
        )
        val remote = FirebaseMapper.parseUser(value)
        assertEquals(listOf(team), remote.teams)
        assertEquals(listOf(player), remote.players)
        assertEquals(RemoteData(), FirebaseMapper.parseUser(null))
    }

    @Test
    fun importer_addsMissingRowsAndDropsDeletedFinishedGames() = runTest {
        val games = FakeGameStore()
        val teams = FakeTeamStore()
        val importer = RemoteDataImporter(
            games.gameDao, teams.teamDao, teams.playerDao,
            GameRepository(games.gameDao, games.eventDao, teams.teamDao)
        )
        // Local: a finished game deleted on another device, a game in progress, an existing team.
        games.gameDao.insertGameInfo(info("gone"))
        games.gameDao.insertGameInfo(info("playing", over = 0))
        teams.teamDao.insertTeam(team("t1", name = "Local name"))

        importer.import(
            RemoteData(
                teams = listOf(team("t1", name = "Remote name"), team("t2")),
                players = listOf(player("t2", "p9")),
                games = listOf(RemoteGame(info("g1"), listOf(row("g1", "p1", 1, pts = 2))))
            )
        )

        assertNull(games.gameDao.getGameInfoById("gone"))
        assertEquals(0, games.gameDao.getGameInfoById("playing")!!.isGameover)
        assertEquals(1, games.gameDao.getGameDataList("g1").size)
        assertEquals("Local name", teams.teamDao.getTeamById("t1")!!.teamName)
        assertTrue(teams.teamDao.getTeamById("t2") != null)
        assertEquals("p9", teams.playerDao.getPlayerById("p9")!!.playerId)

        // Importing again changes nothing.
        importer.import(RemoteData(games = listOf(RemoteGame(info("g1"), listOf(row("g1", "p1", 1, pts = 2))))))
        assertEquals(1, games.gameDao.getGameDataList("g1").size)
    }

    @Test
    fun importer_keepsLocalHistoryWhenRemoteHasNoGames() = runTest {
        val games = FakeGameStore()
        val teams = FakeTeamStore()
        val importer = RemoteDataImporter(
            games.gameDao, teams.teamDao, teams.playerDao,
            GameRepository(games.gameDao, games.eventDao, teams.teamDao)
        )
        games.gameDao.insertGameInfo(info("local"))

        importer.import(RemoteData(teams = listOf(team("t1"))))

        assertTrue(games.gameDao.getGameInfoById("local") != null)
    }
}

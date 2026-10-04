package com.wadexhong.boxscore.database

import com.wadexhong.boxscore.database.dao.GameDao
import com.wadexhong.boxscore.database.dao.GameEventDao
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.gameboxscore.GameStateCalculator
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player

/**
 * Game lifecycle on Room, replacing legacy GameInfoDbHelper / GameDataDbHelper.
 *
 * The event log (`game_event`) is the source of truth while a game is played. `game_info`
 * holds the settings and final score, and `game_data` holds legacy-compatible per-quarter
 * player rows (also the game's roster, starters first), which History and Firebase read.
 */
class GameRepository(
    private val gameDao: GameDao,
    private val eventDao: GameEventDao,
    private val teamDao: TeamDao
) {
    constructor(db: BoxScoreDatabase) : this(db.gameDao(), db.gameEventDao(), db.teamDao())

    /** Legacy writeInitDataIntoModel. Does nothing for a game that already exists. */
    suspend fun startGameIfNew(info: GameInfo) {
        if (info.gameId.isNullOrEmpty() || gameDao.getGameInfoById(info.gameId) != null) return
        gameDao.insertGameInfo(info.toEntity())
        val roster = (info.startingPlayerList.orEmpty() + info.substitutePlayerList.orEmpty())
            .map { it.toRosterEntity(info.yourTeamId) }
        gameDao.insertAllGameData(GameStateCalculator.playerStats(info.gameId, emptyList(), roster))
    }

    suspend fun getUnfinishedGame(): GameInfoEntity? = gameDao.getUnfinishedGame()

    /** Rebuilds the GameInfo a resumed GameBoxScore needs (legacy initGameInfoFromDatabase). */
    suspend fun loadGameInfo(gameId: String): GameInfo? {
        val entity = gameDao.getGameInfoById(gameId) ?: return null
        return entity.toGameInfo(roster(gameId).map { Player(it.playerNumber, it.playerName, it.playerId) })
    }

    /** Writes the score and per-quarter player stats derived from the event log. */
    suspend fun saveProgress(gameId: String): GameInfoEntity? {
        val info = gameDao.getGameInfoById(gameId) ?: return null
        val events = eventDao.getEventsList(gameId)
        // A game started by the legacy app has its stats in game_data but no events: keep them.
        if (events.isEmpty()) return info

        val roster = roster(gameId).map {
            TeamPlayerEntity(
                teamId = info.yourTeamId, playerId = it.playerId, playerName = it.playerName,
                playerNumber = it.playerNumber, center = 0, powerForward = 0, smallForward = 0,
                shootingGuard = 0, pointGuard = 0
            )
        }
        gameDao.replaceGameData(gameId, GameStateCalculator.playerStats(gameId, events, roster))

        val snapshot = GameStateCalculator.calculate(events, emptyList())
        val updated = info.copy(yourScore = snapshot.yourScore, opponentScore = snapshot.opponentScore)
        gameDao.updateGameInfo(updated)
        return updated
    }

    /**
     * Legacy saveAndEndCurrentGame: stores the result, marks the game over and refreshes the
     * team's history count. Returns the finished game, or null if it does not exist.
     */
    suspend fun finishGame(gameId: String): GameInfoEntity? {
        val info = saveProgress(gameId) ?: return null
        val finished = info.copy(isGameover = 1)
        gameDao.updateGameInfo(finished)
        refreshHistoryAmount(info.yourTeamId)
        return finished
    }

    /** Removes every trace of a game (legacy removeGameDataInDataBase). */
    suspend fun discardGame(gameId: String) {
        eventDao.deleteEventsByGameId(gameId)
        gameDao.deleteGameCompletely(gameId)
    }

    /** Legacy HistoryMainPresenter delete: removes the game and returns the team's new count. */
    suspend fun deleteFinishedGame(gameId: String, teamId: String): Int {
        discardGame(gameId)
        return refreshHistoryAmount(teamId)
    }

    suspend fun refreshHistoryAmount(teamId: String): Int {
        val amount = gameDao.countFinishedGames(teamId)
        teamDao.updateHistoryAmount(teamId, amount)
        return amount
    }

    /** One row per player in insertion order, i.e. starters first. */
    private suspend fun roster(gameId: String): List<GameDataEntity> =
        gameDao.getGameDataList(gameId).distinctBy { it.playerId }
}

fun GameInfo.toEntity(): GameInfoEntity = GameInfoEntity(
    gameId = gameId.orEmpty(),
    gameName = gameName.orEmpty(),
    yourTeamId = yourTeamId.orEmpty(),
    yourTeamName = yourTeam.orEmpty(),
    opponentName = opponentName.orEmpty(),
    quarterLength = quarterLength?.toIntOrNull() ?: 0,
    totalQuarter = totalQuarter?.toIntOrNull() ?: 0,
    maxFoul = maxFoul?.toIntOrNull() ?: 0,
    timeoutFirstHalf = timeoutFirstHalf?.toIntOrNull() ?: 0,
    timeoutSecondHalf = timeoutSecondHalf?.toIntOrNull() ?: 0,
    yourScore = 0,
    opponentScore = 0,
    gameDate = gameDate.orEmpty(),
    isGameover = 0
)

/** [roster] in stored order; the first five are the starters, as legacy setPlayerListFromDb did. */
fun GameInfoEntity.toGameInfo(roster: List<Player>): GameInfo = GameInfo().also {
    it.gameId = gameId
    it.gameName = gameName
    it.yourTeamId = yourTeamId
    it.yourTeam = yourTeamName
    it.opponentName = opponentName
    it.quarterLength = quarterLength.toString()
    it.totalQuarter = totalQuarter.toString()
    it.maxFoul = maxFoul.toString()
    it.timeoutFirstHalf = timeoutFirstHalf.toString()
    it.timeoutSecondHalf = timeoutSecondHalf.toString()
    it.gameDate = gameDate
    it.startingPlayerList = ArrayList(roster.take(5))
    it.substitutePlayerList = ArrayList(roster.drop(5))
}

private fun Player.toRosterEntity(teamId: String?) = TeamPlayerEntity(
    teamId = teamId.orEmpty(), playerId = playerId.orEmpty(), playerName = name.orEmpty(),
    playerNumber = number.orEmpty(), center = 0, powerForward = 0, smallForward = 0,
    shootingGuard = 0, pointGuard = 0
)

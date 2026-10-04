package com.wadexhong.boxscore.sync

import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.dao.GameDao
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.dao.TeamDao

/**
 * Applies the user's Firebase data to Room after login (legacy OnCreateDataBaseTask):
 * finished games missing remotely are removed, and teams, players and games that only
 * exist remotely are added. Local rows are never overwritten.
 */
class RemoteDataImporter(
    private val gameDao: GameDao,
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao,
    private val repository: GameRepository
) {
    suspend fun import(remote: RemoteData) {
        val remoteGameIds = remote.games.map { it.info.gameId }.toSet()
        gameDao.getFinishedGameIds()
            .filterNot { it in remoteGameIds }
            .forEach { repository.discardGame(it) }

        remote.games.forEach { game ->
            if (gameDao.getGameInfoById(game.info.gameId) == null) {
                gameDao.insertGameInfo(game.info)
                gameDao.insertAllGameData(game.rows)
            }
        }
        remote.players.forEach { player ->
            if (playerDao.getPlayerById(player.playerId) == null) playerDao.insertPlayer(player)
        }
        remote.teams.forEach { team ->
            if (teamDao.getTeamById(team.teamId) == null) teamDao.insertTeam(team)
        }
    }
}

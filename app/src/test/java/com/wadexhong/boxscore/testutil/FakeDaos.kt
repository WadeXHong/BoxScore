package com.wadexhong.boxscore.testutil

import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

fun team(id: String, name: String = "Team $id") =
    TeamInfoEntity(teamId = id, teamName = name, playersAmount = 0, historyAmount = 0)

fun player(teamId: String, id: String, number: String = id) = TeamPlayerEntity(
    teamId = teamId, playerId = id, playerName = "Player $id", playerNumber = number,
    center = 0, powerForward = 0, smallForward = 0, shootingGuard = 0, pointGuard = 0
)

/** In-memory TeamDao/PlayerDao pair sharing one store, like the real tables. */
class FakeTeamStore {
    val teams = MutableStateFlow<List<TeamInfoEntity>>(emptyList())
    val players = MutableStateFlow<List<TeamPlayerEntity>>(emptyList())

    val teamDao: TeamDao = object : TeamDao {
        override fun getAllTeams(): Flow<List<TeamInfoEntity>> = teams
        override suspend fun getTeamById(teamId: String) = teams.value.find { it.teamId == teamId }
        override suspend fun insertTeam(team: TeamInfoEntity): Long {
            teams.update { list -> list.filterNot { it.teamId == team.teamId } + team }
            return 0
        }
        override suspend fun updateTeam(team: TeamInfoEntity) {
            teams.update { list -> list.map { if (it.teamId == team.teamId) team else it } }
        }
        override suspend fun deleteTeamById(teamId: String) {
            teams.update { list -> list.filterNot { it.teamId == teamId } }
        }
        override suspend fun deletePlayersOfTeam(teamId: String) {
            players.update { list -> list.filterNot { it.teamId == teamId } }
        }
        override suspend fun syncAllTeamPlayersAmounts() {
            teams.update { list -> list.map { t -> t.copy(playersAmount = players.value.count { it.teamId == t.teamId }) } }
        }
    }

    val playerDao: PlayerDao = object : PlayerDao {
        override fun getPlayersByTeamId(teamId: String) = players.map { list -> list.filter { it.teamId == teamId } }
        override fun getAllPlayers(): Flow<List<TeamPlayerEntity>> = players
        override suspend fun insertPlayer(player: TeamPlayerEntity): Long {
            players.update { it + player }
            return 0
        }
        override suspend fun updatePlayer(player: TeamPlayerEntity) {
            players.update { list -> list.map { if (it.playerId == player.playerId) player else it } }
        }
        override suspend fun deletePlayerById(playerId: String) {
            players.update { list -> list.filterNot { it.playerId == playerId } }
        }
        override suspend fun deletePlayersByTeamId(teamId: String) {
            players.update { list -> list.filterNot { it.teamId == teamId } }
        }
        override suspend fun syncTeamPlayersAmount(teamId: String) {
            teams.update { list ->
                list.map { if (it.teamId == teamId) it.copy(playersAmount = players.value.count { p -> p.teamId == teamId }) else it }
            }
        }
    }
}

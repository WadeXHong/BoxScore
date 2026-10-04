package com.wadexhong.boxscore.sync

import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

/** Mirrors TeamManage changes to the cloud (legacy TeamDbHelper called Create/Delete). */
interface TeamSync {
    fun teamCreated(team: TeamInfoEntity)
    suspend fun teamDeleted(teamId: String)
    fun playerCreated(player: TeamPlayerEntity, newPlayersAmount: Int)
    fun playerDeleted(teamId: String, playerId: String, newPlayersAmount: Int)

    companion object {
        val Firebase: TeamSync = object : TeamSync {
            override fun teamCreated(team: TeamInfoEntity) = FirebaseSync.createTeam(team)
            override suspend fun teamDeleted(teamId: String) = FirebaseSync.deleteTeam(teamId)
            override fun playerCreated(player: TeamPlayerEntity, newPlayersAmount: Int) =
                FirebaseSync.createPlayer(player, newPlayersAmount)
            override fun playerDeleted(teamId: String, playerId: String, newPlayersAmount: Int) =
                FirebaseSync.deletePlayer(teamId, playerId, newPlayersAmount)
        }
    }
}

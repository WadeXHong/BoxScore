package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wadexhong.boxscore.Constants.TeamInfoDBContract
import com.wadexhong.boxscore.Constants.TeamPlayersContract
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId")
    fun getPlayersByTeamId(teamId: String): Flow<List<TeamPlayerEntity>>

    @Query("SELECT * FROM ${TeamPlayersContract.TABLE_NAME}")
    fun getAllPlayers(): Flow<List<TeamPlayerEntity>>

    @Query("SELECT * FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_PLAYER_ID} = :playerId LIMIT 1")
    suspend fun getPlayerById(playerId: String): TeamPlayerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: TeamPlayerEntity): Long

    @Update
    suspend fun updatePlayer(player: TeamPlayerEntity)

    @Query("DELETE FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_PLAYER_ID} = :playerId")
    suspend fun deletePlayerById(playerId: String)
    
    @Query("DELETE FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun deletePlayersByTeamId(teamId: String)

    /** Recomputes the team's stored player count from its roster (legacy TeamDbHelper kept it in sync). */
    @Query(
        "UPDATE ${TeamInfoDBContract.TABLE_NAME} SET ${TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT} = " +
            "(SELECT COUNT(*) FROM ${TeamPlayersContract.TABLE_NAME} " +
            "WHERE ${TeamPlayersContract.TABLE_NAME}.${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId) " +
            "WHERE ${TeamInfoDBContract.TABLE_NAME}.${TeamInfoDBContract.COLUMN_NAME_TEAM_ID} = :teamId"
    )
    suspend fun syncTeamPlayersAmount(teamId: String)

    @Transaction
    suspend fun insertPlayerAndSyncAmount(player: TeamPlayerEntity) {
        insertPlayer(player)
        syncTeamPlayersAmount(player.teamId)
    }

    @Transaction
    suspend fun deletePlayerAndSyncAmount(teamId: String, playerId: String) {
        deletePlayerById(playerId)
        syncTeamPlayersAmount(teamId)
    }
}

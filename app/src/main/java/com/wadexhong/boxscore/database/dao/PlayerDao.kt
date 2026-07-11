package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wadexhong.boxscore.Constants.TeamPlayersContract
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId")
    fun getPlayersByTeamId(teamId: String): Flow<List<TeamPlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: TeamPlayerEntity): Long

    @Update
    suspend fun updatePlayer(player: TeamPlayerEntity)

    @Query("DELETE FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_PLAYER_ID} = :playerId")
    suspend fun deletePlayerById(playerId: String)
    
    @Query("DELETE FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun deletePlayersByTeamId(teamId: String)
}

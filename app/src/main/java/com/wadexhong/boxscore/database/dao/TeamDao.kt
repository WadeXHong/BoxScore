package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wadexhong.boxscore.Constants.TeamInfoDBContract
import com.wadexhong.boxscore.Constants.TeamPlayersContract
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM ${TeamInfoDBContract.TABLE_NAME}")
    fun getAllTeams(): Flow<List<TeamInfoEntity>>

    @Query("SELECT * FROM ${TeamInfoDBContract.TABLE_NAME} WHERE ${TeamInfoDBContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun getTeamById(teamId: String): TeamInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamInfoEntity): Long

    @Update
    suspend fun updateTeam(team: TeamInfoEntity)

    @Query("DELETE FROM ${TeamInfoDBContract.TABLE_NAME} WHERE ${TeamInfoDBContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun deleteTeamById(teamId: String)

    @Query("DELETE FROM ${TeamPlayersContract.TABLE_NAME} WHERE ${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun deletePlayersOfTeam(teamId: String)

    @Query("UPDATE ${TeamInfoDBContract.TABLE_NAME} SET ${TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT} = :amount WHERE ${TeamInfoDBContract.COLUMN_NAME_TEAM_ID} = :teamId")
    suspend fun updateHistoryAmount(teamId: String, amount: Int)

    /** Legacy deleteTeamInDb removed the team together with its roster. */
    @Transaction
    suspend fun deleteTeamWithPlayers(teamId: String) {
        deleteTeamById(teamId)
        deletePlayersOfTeam(teamId)
    }

    /** Repairs every team's stored player count from the roster table. */
    @Query(
        "UPDATE ${TeamInfoDBContract.TABLE_NAME} SET ${TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT} = " +
            "(SELECT COUNT(*) FROM ${TeamPlayersContract.TABLE_NAME} " +
            "WHERE ${TeamPlayersContract.TABLE_NAME}.${TeamPlayersContract.COLUMN_NAME_TEAM_ID} = " +
            "${TeamInfoDBContract.TABLE_NAME}.${TeamInfoDBContract.COLUMN_NAME_TEAM_ID})"
    )
    suspend fun syncAllTeamPlayersAmounts()
}

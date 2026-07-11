package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wadexhong.boxscore.Constants.TeamInfoDBContract
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
}

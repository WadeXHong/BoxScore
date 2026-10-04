package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wadexhong.boxscore.Constants.TeamInfoDBContract

// Unique business key: imports check-then-insert, and REPLACE must replace rather than duplicate.
@Entity(
    tableName = TeamInfoDBContract.TABLE_NAME,
    indices = [Index(value = [TeamInfoDBContract.COLUMN_NAME_TEAM_ID], unique = true)]
)
data class TeamInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TeamInfoDBContract._ID)
    val id: Long = 0,

    @ColumnInfo(name = TeamInfoDBContract.COLUMN_NAME_TEAM_NAME)
    val teamName: String,

    @ColumnInfo(name = TeamInfoDBContract.COLUMN_NAME_TEAM_ID)
    val teamId: String,

    @ColumnInfo(name = TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT)
    val playersAmount: Int,

    @ColumnInfo(name = TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT)
    val historyAmount: Int
)

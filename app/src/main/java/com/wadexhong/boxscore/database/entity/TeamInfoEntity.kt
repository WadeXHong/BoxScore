package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wadexhong.boxscore.Constants.TeamInfoDBContract

@Entity(tableName = TeamInfoDBContract.TABLE_NAME)
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

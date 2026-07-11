package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wadexhong.boxscore.Constants.TeamPlayersContract

@Entity(tableName = TeamPlayersContract.TABLE_NAME)
data class TeamPlayerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TeamPlayersContract._ID)
    val id: Long = 0,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_TEAM_ID)
    val teamId: String,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAYER_ID)
    val playerId: String,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAYER_NAME)
    val playerName: String,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER)
    val playerNumber: String,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAY_C)
    val center: Int,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAY_PF)
    val powerForward: Int,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAY_SF)
    val smallForward: Int,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAY_SG)
    val shootingGuard: Int,

    @ColumnInfo(name = TeamPlayersContract.COLUMN_NAME_PLAY_PG)
    val pointGuard: Int
)

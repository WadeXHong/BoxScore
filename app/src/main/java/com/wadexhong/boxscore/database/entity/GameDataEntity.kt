package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wadexhong.boxscore.Constants.GameDataDBContract

@Entity(tableName = GameDataDBContract.TABLE_NAME)
data class GameDataEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = GameDataDBContract._ID)
    val id: Long = 0,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_GAME_ID)
    val gameId: String,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_QUARTER)
    val quarter: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_PLAYER_ID)
    val playerId: String,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER)
    val playerNumber: String,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_PLAYER_NAME)
    val playerName: String,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_POINTS)
    val pts: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE)
    val fgm: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED)
    val fga: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE)
    val tpm: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED)
    val tpa: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE)
    val ftm: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED)
    val fta: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND)
    val oreb: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND)
    val dreb: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_ASSIST)
    val ast: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_STEAL)
    val stl: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_BLOCK)
    val blk: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL)
    val pf: Int,

    @ColumnInfo(name = GameDataDBContract.COLUMN_NAME_TURNOVER)
    val tov: Int
)

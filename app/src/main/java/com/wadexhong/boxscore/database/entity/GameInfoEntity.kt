package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wadexhong.boxscore.Constants.GameInfoDBContract

// Unique business key: imports check-then-insert, and REPLACE must replace rather than duplicate.
@Entity(
    tableName = GameInfoDBContract.TABLE_NAME,
    indices = [Index(value = [GameInfoDBContract.COLUMN_NAME_GAME_ID], unique = true)]
)
data class GameInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = GameInfoDBContract._ID)
    val id: Long = 0,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_GAME_ID)
    val gameId: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_GAME_NAME)
    val gameName: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID)
    val yourTeamId: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_YOUR_TEAM)
    val yourTeamName: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)
    val opponentName: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH)
    val quarterLength: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER)
    val totalQuarter: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_MAX_FOUL)
    val maxFoul: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF)
    val timeoutFirstHalf: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF)
    val timeoutSecondHalf: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE)
    val yourScore: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE)
    val opponentScore: Int,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_GAME_DATE)
    val gameDate: String,
    
    @ColumnInfo(name = GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER)
    val isGameover: Int
)

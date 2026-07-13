package com.wadexhong.boxscore.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_event")
data class GameEventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Long = 0,

    @ColumnInfo(name = "game_id")
    val gameId: String,

    @ColumnInfo(name = "action_type")
    val actionType: Int, // Refers to Constants.RecordDataType

    @ColumnInfo(name = "player_id")
    val playerId: String? = null, // Null if team-level event

    @ColumnInfo(name = "quarter")
    val quarter: Int,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

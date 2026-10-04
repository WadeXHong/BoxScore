package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wadexhong.boxscore.database.entity.GameEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameEventDao {
    @Query("SELECT * FROM game_event WHERE game_id = :gameId ORDER BY timestamp ASC")
    fun getEventsByGameId(gameId: String): Flow<List<GameEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: GameEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<GameEventEntity>)

    @Update
    suspend fun updateEvent(event: GameEventEntity)

    @Query("DELETE FROM game_event WHERE _id = :eventId")
    suspend fun deleteEventById(eventId: Long)

    @Query("DELETE FROM game_event WHERE game_id = :gameId")
    suspend fun deleteEventsByGameId(gameId: String)
}

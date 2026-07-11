package com.wadexhong.boxscore.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wadexhong.boxscore.Constants.GameInfoDBContract
import com.wadexhong.boxscore.Constants.GameDataDBContract
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.database.entity.GameDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    // --- Game Info ---
    @Query("SELECT * FROM ${GameInfoDBContract.TABLE_NAME} ORDER BY ${GameInfoDBContract._ID} DESC")
    fun getAllGameInfos(): Flow<List<GameInfoEntity>>

    @Query("SELECT * FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_GAME_ID} = :gameId")
    suspend fun getGameInfoById(gameId: String): GameInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameInfo(gameInfo: GameInfoEntity): Long

    @Update
    suspend fun updateGameInfo(gameInfo: GameInfoEntity)

    @Query("DELETE FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_GAME_ID} = :gameId")
    suspend fun deleteGameInfoById(gameId: String)

    // --- Game Data ---
    @Query("SELECT * FROM ${GameDataDBContract.TABLE_NAME} WHERE ${GameDataDBContract.COLUMN_NAME_GAME_ID} = :gameId")
    fun getGameDataByGameId(gameId: String): Flow<List<GameDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameData(gameData: GameDataEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGameData(gameDataList: List<GameDataEntity>)

    @Update
    suspend fun updateGameData(gameData: GameDataEntity)

    @Query("DELETE FROM ${GameDataDBContract.TABLE_NAME} WHERE ${GameDataDBContract.COLUMN_NAME_GAME_ID} = :gameId")
    suspend fun deleteGameDataByGameId(gameId: String)

    // --- Transactions ---
    @Transaction
    suspend fun deleteGameCompletely(gameId: String) {
        deleteGameInfoById(gameId)
        deleteGameDataByGameId(gameId)
    }
}

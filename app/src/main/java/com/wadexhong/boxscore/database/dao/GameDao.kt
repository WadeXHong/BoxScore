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

    /** The game still being played, if any (legacy kept its id in SharedPreferences PLAYING_GAME). */
    @Query("SELECT * FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER} = 0 ORDER BY ${GameInfoDBContract._ID} DESC LIMIT 1")
    suspend fun getUnfinishedGame(): GameInfoEntity?

    /** Legacy GameInfoDbHelper.getGameHistory: finished games, newest date first. */
    @Query("SELECT * FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER} = 1 ORDER BY ${GameInfoDBContract.COLUMN_NAME_GAME_DATE} DESC, ${GameInfoDBContract._ID} DESC")
    fun getFinishedGames(): Flow<List<GameInfoEntity>>

    @Query("SELECT ${GameInfoDBContract.COLUMN_NAME_GAME_ID} FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER} = 1")
    suspend fun getFinishedGameIds(): List<String>

    @Query("SELECT COUNT(*) FROM ${GameInfoDBContract.TABLE_NAME} WHERE ${GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID} = :teamId AND ${GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER} = 1")
    suspend fun countFinishedGames(teamId: String): Int

    // --- Game Data ---
    @Query("SELECT * FROM ${GameDataDBContract.TABLE_NAME} WHERE ${GameDataDBContract.COLUMN_NAME_GAME_ID} = :gameId ORDER BY ${GameDataDBContract._ID}")
    fun getGameDataByGameId(gameId: String): Flow<List<GameDataEntity>>

    /** Rows in insertion order: a game's roster starts with its five starters. */
    @Query("SELECT * FROM ${GameDataDBContract.TABLE_NAME} WHERE ${GameDataDBContract.COLUMN_NAME_GAME_ID} = :gameId ORDER BY ${GameDataDBContract._ID}")
    suspend fun getGameDataList(gameId: String): List<GameDataEntity>

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

    /** Replaces a game's player stat rows with freshly aggregated ones. */
    @Transaction
    suspend fun replaceGameData(gameId: String, rows: List<GameDataEntity>) {
        deleteGameDataByGameId(gameId)
        insertAllGameData(rows)
    }
}

package com.wadexhong.boxscore.database

import android.content.Context
import android.util.Log
import com.wadexhong.boxscore.BoxScore
import com.wadexhong.boxscore.Constants
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.modelhelper.SharedPreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import androidx.room.withTransaction
import android.database.Cursor

object LegacyDataMigrator {

    private const val TAG = "LegacyDataMigrator"
    private const val PREF_KEY_HAS_MIGRATED = "has_migrated_legacy_data"

    fun migrateIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences("boxscore_migration", Context.MODE_PRIVATE)
        val hasMigrated = prefs.getBoolean(PREF_KEY_HAS_MIGRATED, false)
        if (hasMigrated) {
            Log.d(TAG, "Legacy data already migrated. Skipping.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Starting legacy data migration...")
                val db = BoxScoreDatabase.getDatabase(context)
                
                // All or nothing: a half-finished run would make the retry treat already
                // inserted games as existing and skip their stats.
                DataWriteLock.mutex.withLock {
                    db.withTransaction {
                        migrateTeamsAndPlayers(context, db)
                        migrateGamesAndData(context, db)
                    }
                }
                
                withContext(Dispatchers.Main) {
                    prefs.edit().putBoolean(PREF_KEY_HAS_MIGRATED, true).apply()
                    Log.d(TAG, "Legacy data migration completed successfully.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to migrate legacy data", e)
            }
        }
    }

    private suspend fun migrateTeamsAndPlayers(context: Context, db: BoxScoreDatabase) {
        val teamDao = db.teamDao()
        val playerDao = db.playerDao()
        
        // Use the old SQLiteOpenHelper to read existing data
        val oldTeamHelper = BoxScore.getTeamDbHelper()
        val oldSqlDb = oldTeamHelper.readableDatabase

        try {
            // 1. Migrate TeamInfo
            val teamCursor = oldSqlDb.query(
                Constants.TeamInfoDBContract.TABLE_NAME, null, null, null, null, null, null
            )
            
            while (teamCursor.moveToNext()) {
                val teamName = teamCursor.str(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_NAME)
                val teamId = teamCursor.str(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_ID)
                val playersAmount = teamCursor.int(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT)
                val historyAmount = teamCursor.int(Constants.TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT)
                
                val newTeam = TeamInfoEntity(
                    teamName = teamName,
                    teamId = teamId,
                    playersAmount = playersAmount,
                    historyAmount = historyAmount
                )
                // Insert into Room unless the cloud download already added it
                if (teamDao.getTeamById(teamId) == null) teamDao.insertTeam(newTeam)
                Log.d(TAG, "Migrated Team: $teamName")
            }
            teamCursor.close()

            // 2. Migrate TeamPlayers
            val playerCursor = oldSqlDb.query(
                Constants.TeamPlayersContract.TABLE_NAME, null, null, null, null, null, null
            )
            
            while (playerCursor.moveToNext()) {
                val teamId = playerCursor.str(Constants.TeamPlayersContract.COLUMN_NAME_TEAM_ID)
                val playerId = playerCursor.str(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_ID)
                val playerName = playerCursor.str(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NAME)
                val playerNumber = playerCursor.str(Constants.TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER)
                
                val center = playerCursor.int(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_C)
                val pf = playerCursor.int(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PF)
                val sf = playerCursor.int(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SF)
                val sg = playerCursor.int(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_SG)
                val pg = playerCursor.int(Constants.TeamPlayersContract.COLUMN_NAME_PLAY_PG)

                val newPlayer = TeamPlayerEntity(
                    teamId = teamId,
                    playerId = playerId,
                    playerName = playerName,
                    playerNumber = playerNumber,
                    center = center,
                    powerForward = pf,
                    smallForward = sf,
                    shootingGuard = sg,
                    pointGuard = pg
                )
                // Insert into Room unless the cloud download already added it
                if (playerDao.getPlayerById(playerId) == null) playerDao.insertPlayer(newPlayer)
            }
            playerCursor.close()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error migrating teams and players", e)
            throw e
        }
    }

    private suspend fun migrateGamesAndData(context: Context, db: BoxScoreDatabase) {
        val gameDao = db.gameDao()
        
        // Use the old SQLiteOpenHelpers to read existing data
        val oldGameInfoHelper = BoxScore.getGameInfoDbHelper()
        val oldGameInfoDb = oldGameInfoHelper.readableDatabase

        val oldGameDataHelper = BoxScore.getGameDataDbHelper()
        val oldGameDataDb = oldGameDataHelper.readableDatabase

        try {
            // Games already in Room (e.g. from the cloud download) keep their own rows.
            val existingGameIds = mutableSetOf<String>()

            // 1. Migrate GameInfo
            val infoCursor = oldGameInfoDb.query(
                Constants.GameInfoDBContract.TABLE_NAME, null, null, null, null, null, null
            )
            
            while (infoCursor.moveToNext()) {
                val gameId = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_GAME_ID)
                val gameName = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_GAME_NAME)
                val yourTeamId = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID)
                val yourTeamName = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM)
                val opponentName = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME)
                val quarterLength = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH)
                val totalQuarter = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER)
                val maxFoul = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_MAX_FOUL)
                val timeoutFirstHalf = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF)
                val timeoutSecondHalf = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF)
                val yourScore = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE)
                val opponentScore = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE)
                val gameDate = infoCursor.str(Constants.GameInfoDBContract.COLUMN_NAME_GAME_DATE)
                val isGameOver = infoCursor.int(Constants.GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER)
                
                val newGameInfo = com.wadexhong.boxscore.database.entity.GameInfoEntity(
                    gameId = gameId,
                    gameName = gameName,
                    yourTeamId = yourTeamId,
                    yourTeamName = yourTeamName,
                    opponentName = opponentName,
                    quarterLength = quarterLength,
                    totalQuarter = totalQuarter,
                    maxFoul = maxFoul,
                    timeoutFirstHalf = timeoutFirstHalf,
                    timeoutSecondHalf = timeoutSecondHalf,
                    yourScore = yourScore,
                    opponentScore = opponentScore,
                    gameDate = gameDate,
                    isGameover = isGameOver
                )
                if (gameDao.getGameInfoById(gameId) != null) {
                    existingGameIds += gameId
                    continue
                }
                gameDao.insertGameInfo(newGameInfo)
                Log.d(TAG, "Migrated GameInfo: $gameId")
            }
            infoCursor.close()

            // 2. Migrate GameData
            val dataCursor = oldGameDataDb.query(
                Constants.GameDataDBContract.TABLE_NAME, null, null, null, null, null, null
            )
            
            while (dataCursor.moveToNext()) {
                val gameId = dataCursor.str(Constants.GameDataDBContract.COLUMN_NAME_GAME_ID)
                val quarter = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_QUARTER)
                val playerId = dataCursor.str(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_ID)
                val playerNumber = dataCursor.str(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER)
                val playerName = dataCursor.str(Constants.GameDataDBContract.COLUMN_NAME_PLAYER_NAME)
                val pts = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_POINTS)
                val fgm = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE)
                val fga = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED)
                val tpm = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE)
                val tpa = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED)
                val ftm = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE)
                val fta = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED)
                val oreb = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND)
                val dreb = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND)
                val ast = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_ASSIST)
                val stl = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_STEAL)
                val blk = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_BLOCK)
                val pf = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL)
                val tov = dataCursor.int(Constants.GameDataDBContract.COLUMN_NAME_TURNOVER)

                val newGameData = com.wadexhong.boxscore.database.entity.GameDataEntity(
                    gameId = gameId,
                    quarter = quarter,
                    playerId = playerId,
                    playerNumber = playerNumber,
                    playerName = playerName,
                    pts = pts,
                    fgm = fgm,
                    fga = fga,
                    tpm = tpm,
                    tpa = tpa,
                    ftm = ftm,
                    fta = fta,
                    oreb = oreb,
                    dreb = dreb,
                    ast = ast,
                    stl = stl,
                    blk = blk,
                    pf = pf,
                    tov = tov
                )
                if (gameId !in existingGameIds) gameDao.insertGameData(newGameData)
            }
            dataCursor.close()
            Log.d(TAG, "Migrated GameData successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error migrating game info and data", e)
            throw e
        }
    }

    /** Legacy rows can hold NULL (e.g. fields missing from a Firebase download). */
    private fun Cursor.str(column: String): String = getString(getColumnIndexOrThrow(column)) ?: ""

    private fun Cursor.int(column: String): Int = getInt(getColumnIndexOrThrow(column))
}

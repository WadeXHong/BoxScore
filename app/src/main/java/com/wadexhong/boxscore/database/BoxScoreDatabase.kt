package com.wadexhong.boxscore.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wadexhong.boxscore.database.dao.GameDao
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Database(
    entities = [
        GameInfoEntity::class,
        GameDataEntity::class,
        TeamInfoEntity::class,
        TeamPlayerEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BoxScoreDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun teamDao(): TeamDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: BoxScoreDatabase? = null

        fun getDatabase(context: Context): BoxScoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoxScoreDatabase::class.java,
                    "boxscore_database"
                )
                // If you want to destructive migrate (wipe data on schema change during development)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

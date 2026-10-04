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
import com.wadexhong.boxscore.database.dao.GameEventDao
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

@Database(
    entities = [
        GameInfoEntity::class,
        GameDataEntity::class,
        TeamInfoEntity::class,
        TeamPlayerEntity::class,
        GameEventEntity::class
    ],
    version = 2,
    // Schemas are exported to app/schemas so every future version can ship a tested Migration.
    exportSchema = true
)
abstract class BoxScoreDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun teamDao(): TeamDao
    abstract fun playerDao(): PlayerDao
    abstract fun gameEventDao(): GameEventDao

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
                // Version 1 only existed on development builds. Every later schema change must
                // add a Migration here (see app/schemas) instead of wiping user data.
                .fallbackToDestructiveMigrationFrom(1)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

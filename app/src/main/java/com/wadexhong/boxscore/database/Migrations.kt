package com.wadexhong.boxscore.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room schema migrations. Every version after 2 must ship one; see app/schemas for the
 * exported schema of each version.
 */
object Migrations {

    /** Business keys that must be unique, as (table, column). */
    internal val UNIQUE_KEYS = listOf(
        "game_info" to "game_id",
        "team_info" to "team_id",
        "team_player" to "player_id"
    )

    /** SQL run by [MIGRATION_2_3], exposed so a unit test can check it against the schema. */
    internal val SQL_2_3: List<String> = UNIQUE_KEYS.flatMap { (table, column) ->
        listOf(
            // Keep the oldest row of each key: concurrent imports could insert the same one twice.
            "DELETE FROM `$table` WHERE `_id` NOT IN (SELECT MIN(`_id`) FROM `$table` GROUP BY `$column`)",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_${table}_$column` ON `$table` (`$column`)"
        )
    }

    /** 2 -> 3: unique indexes on game_info.game_id, team_info.team_id, team_player.player_id. */
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            SQL_2_3.forEach(db::execSQL)
        }
    }

    val ALL: Array<Migration> = arrayOf(MIGRATION_2_3)
}

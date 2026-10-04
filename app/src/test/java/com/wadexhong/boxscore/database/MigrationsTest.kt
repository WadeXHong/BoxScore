package com.wadexhong.boxscore.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Checks the hand-written migration against the schemas Room exports to app/schemas, so the
 * migrated database matches what Room validates on open.
 */
class MigrationsTest {

    private fun schema(version: Int): String =
        File("schemas/com.wadexhong.boxscore.database.BoxScoreDatabase/$version.json").readText()

    /** Index createSql of every entity; each entity's block starts at its "tableName". */
    private fun indexSql(json: String): Set<String> =
        json.split("\"tableName\": \"").drop(1).flatMap { block ->
            val table = block.substringBefore('"')
            Regex("\"createSql\": \"(CREATE (UNIQUE )?INDEX.*?)\"").findAll(block)
                .map { it.groupValues[1].replace("\${TABLE_NAME}", table) }
                .toList()
        }.toSet()

    @Test
    fun migration2to3_createsExactlyTheIndexesOfVersion3() {
        assertTrue(indexSql(schema(2)).isEmpty())
        val created = Migrations.SQL_2_3.filter { it.startsWith("CREATE") }.toSet()
        assertEquals(indexSql(schema(3)), created)
    }

    @Test
    fun migration2to3_dedupesBeforeEachIndex() {
        Migrations.UNIQUE_KEYS.forEach { (table, column) ->
            val delete = Migrations.SQL_2_3.indexOfFirst { it.startsWith("DELETE FROM `$table`") && "GROUP BY `$column`" in it }
            val create = Migrations.SQL_2_3.indexOfFirst { it.contains("index_${table}_$column") }
            assertTrue("$table: dedupe must run before the unique index", delete in 0 until create)
        }
    }
}

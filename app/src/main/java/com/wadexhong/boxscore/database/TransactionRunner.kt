package com.wadexhong.boxscore.database

import androidx.room.withTransaction

/** Runs a block in a database transaction; [None] runs it directly (unit tests). */
interface TransactionRunner {
    suspend fun <R> run(block: suspend () -> R): R

    companion object {
        val None: TransactionRunner = object : TransactionRunner {
            override suspend fun <R> run(block: suspend () -> R): R = block()
        }

        fun of(db: BoxScoreDatabase): TransactionRunner = object : TransactionRunner {
            override suspend fun <R> run(block: suspend () -> R): R = db.withTransaction { block() }
        }
    }
}

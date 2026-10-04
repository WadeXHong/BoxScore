package com.wadexhong.boxscore.database

import kotlinx.coroutines.sync.Mutex

/**
 * Serialises the bulk writers that run around app start and sign-out: the legacy SQLite
 * migration, the cloud download and the sign-out wipe. Without it their check-then-insert
 * steps could interleave and duplicate rows, or a download could land after the wipe.
 */
object DataWriteLock {
    val mutex = Mutex()
}

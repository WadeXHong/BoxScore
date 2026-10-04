package com.wadexhong.boxscore.setting

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/** Startup access to the brightness saved by Settings (Java-friendly). */
object BrightnessStore {

    /**
     * Blocks on the first DataStore read, which also migrates the legacy SharedPreferences
     * value. It runs once in Application.onCreate so every Activity can apply it on resume.
     */
    @JvmStatic
    fun load(context: Context): Float = runBlocking {
        SettingsRepository(context.applicationContext.dataStore).brightnessFlow.first()
    }
}

package com.wadexhong.boxscore.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, context.packageName))
    }
)

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    
    companion object {
        val DOUBLE_UP = intPreferencesKey("DOUBLE_UP")
        val DOUBLE_DOWN = intPreferencesKey("DOUBLE_DOWN")
        val DOUBLE_LEFT = intPreferencesKey("DOUBLE_LEFT")
        val DOUBLE_RIGHT = intPreferencesKey("DOUBLE_RIGHT")
        val TRIPLE_UP = intPreferencesKey("TRIPLE_UP")
        val TRIPLE_DOWN = intPreferencesKey("TRIPLE_DOWN")
        val TRIPLE_LEFT = intPreferencesKey("TRIPLE_LEFT")
        val TRIPLE_RIGHT = intPreferencesKey("TRIPLE_RIGHT")
        val BRIGHTNESS = floatPreferencesKey("BRIGHTNESS")
    }

    val doubleUpFlow: Flow<Int> = dataStore.data.map { it[DOUBLE_UP] ?: -1 }
    val doubleDownFlow: Flow<Int> = dataStore.data.map { it[DOUBLE_DOWN] ?: -1 }
    val doubleLeftFlow: Flow<Int> = dataStore.data.map { it[DOUBLE_LEFT] ?: -1 }
    val doubleRightFlow: Flow<Int> = dataStore.data.map { it[DOUBLE_RIGHT] ?: -1 }
    val tripleUpFlow: Flow<Int> = dataStore.data.map { it[TRIPLE_UP] ?: -1 }
    val tripleDownFlow: Flow<Int> = dataStore.data.map { it[TRIPLE_DOWN] ?: -1 }
    val tripleLeftFlow: Flow<Int> = dataStore.data.map { it[TRIPLE_LEFT] ?: -1 }
    val tripleRightFlow: Flow<Int> = dataStore.data.map { it[TRIPLE_RIGHT] ?: -1 }
    
    val brightnessFlow: Flow<Float> = dataStore.data.map { it[BRIGHTNESS] ?: -1f }

    suspend fun updateGesture(key: Preferences.Key<Int>, actionId: Int) {
        dataStore.edit { preferences ->
            preferences[key] = actionId
        }
    }
    
    suspend fun updateBrightness(brightness: Float) {
        dataStore.edit { preferences ->
            preferences[BRIGHTNESS] = brightness
        }
    }
}

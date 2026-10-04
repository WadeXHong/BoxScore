package com.wadexhong.boxscore.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.wadexhong.boxscore.database.BoxScoreDatabase

data class SettingsUiState(
    val doubleUp: Int = -1,
    val doubleDown: Int = -1,
    val doubleLeft: Int = -1,
    val doubleRight: Int = -1,
    val tripleUp: Int = -1,
    val tripleDown: Int = -1,
    val tripleLeft: Int = -1,
    val tripleRight: Int = -1,
    val brightness: Float = -1f
)

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val database: BoxScoreDatabase? = null
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        repository.doubleUpFlow,
        repository.doubleDownFlow,
        repository.doubleLeftFlow,
        repository.doubleRightFlow,
        repository.tripleUpFlow,
        repository.tripleDownFlow,
        repository.tripleLeftFlow,
        repository.tripleRightFlow,
        repository.brightnessFlow
    ) { args ->
        SettingsUiState(
            doubleUp = args[0] as Int,
            doubleDown = args[1] as Int,
            doubleLeft = args[2] as Int,
            doubleRight = args[3] as Int,
            tripleUp = args[4] as Int,
            tripleDown = args[5] as Int,
            tripleLeft = args[6] as Int,
            tripleRight = args[7] as Int,
            brightness = args[8] as Float
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateGesture(key: Preferences.Key<Int>, actionId: Int) {
        viewModelScope.launch {
            repository.updateGesture(key, actionId)
        }
    }
    
    fun updateBrightness(brightness: Float) {
        viewModelScope.launch {
            repository.updateBrightness(brightness)
        }
    }
    
    /** Legacy SettingPresenter.signOut: wipe local data, then sign out of Firebase. */
    fun signOut(onSignOutComplete: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                com.wadexhong.boxscore.BoxScore.getGameDataDbHelper().deleteAll(null)
                com.wadexhong.boxscore.BoxScore.getGameInfoDbHelper().deleteAll(null)
                com.wadexhong.boxscore.BoxScore.getTeamDbHelper().deleteAll()
                database?.clearAllTables()
            }
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
            onSignOutComplete()
        }
    }
}

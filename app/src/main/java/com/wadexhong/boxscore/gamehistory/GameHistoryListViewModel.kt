package com.wadexhong.boxscore.gamehistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameHistoryListViewModel(application: Application) : AndroidViewModel(application) {
    private val gameDao = BoxScoreDatabase.getDatabase(application).gameDao()

    private val _gameInfos = MutableStateFlow<List<GameInfoEntity>>(emptyList())
    val gameInfos: StateFlow<List<GameInfoEntity>> = _gameInfos.asStateFlow()

    init {
        viewModelScope.launch {
            gameDao.getAllGameInfos().collectLatest { list ->
                _gameInfos.value = list.filter { it.isGameover == 1 }
            }
        }
    }
}

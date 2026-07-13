package com.wadexhong.boxscore.gamehistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class TeamStats(
    val totalPoints: Int = 0,
    val totalFgm: Int = 0,
    val totalFga: Int = 0,
    val total3pm: Int = 0,
    val total3pa: Int = 0,
    val totalFtm: Int = 0,
    val totalFta: Int = 0,
    val totalReb: Int = 0,
    val totalAst: Int = 0
)

data class DetailUiState(
    val gameInfo: GameInfoEntity? = null,
    val teamStats: TeamStats = TeamStats(),
    val gameDataList: List<GameDataEntity> = emptyList()
)

class GameHistoryDetailViewModel(
    application: Application,
    private val gameId: String
) : AndroidViewModel(application) {

    private val gameDao = BoxScoreDatabase.getDatabase(application).gameDao()

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val info = gameDao.getGameInfoById(gameId)
            gameDao.getGameDataByGameId(gameId).collectLatest { dataList ->
                val stats = calculateTeamStats(dataList)
                _uiState.value = DetailUiState(
                    gameInfo = info,
                    teamStats = stats,
                    gameDataList = dataList
                )
            }
        }
    }

    private fun calculateTeamStats(data: List<GameDataEntity>): TeamStats {
        return TeamStats(
            totalPoints = data.sumOf { it.pts },
            totalFgm = data.sumOf { it.fgm },
            totalFga = data.sumOf { it.fga },
            total3pm = data.sumOf { it.tpm },
            total3pa = data.sumOf { it.tpa },
            totalFtm = data.sumOf { it.ftm },
            totalFta = data.sumOf { it.fta },
            totalReb = data.sumOf { it.oreb + it.dreb },
            totalAst = data.sumOf { it.ast }
        )
    }
}

class GameHistoryDetailViewModelFactory(
    private val application: Application,
    private val gameId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameHistoryDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameHistoryDetailViewModel(application, gameId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

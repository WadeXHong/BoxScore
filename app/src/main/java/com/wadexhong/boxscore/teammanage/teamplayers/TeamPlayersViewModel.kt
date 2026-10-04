package com.wadexhong.boxscore.teammanage.teamplayers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.wadexhong.boxscore.sync.TeamSync
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class TeamPlayersUiState(
    val players: List<TeamPlayerEntity> = emptyList(),
    val isLoading: Boolean = false
)

class TeamPlayersViewModel(
    private val teamId: String,
    private val playerDao: PlayerDao,
    private val sync: TeamSync = TeamSync.Firebase
) : ViewModel() {

    val uiState: StateFlow<TeamPlayersUiState> = playerDao.getPlayersByTeamId(teamId)
        .map { players ->
            TeamPlayersUiState(players = players, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TeamPlayersUiState(isLoading = true)
        )

    fun deletePlayer(playerId: String) {
        viewModelScope.launch {
            playerDao.deletePlayerAndSyncAmount(teamId, playerId)
            sync.playerDeleted(teamId, playerId, playerDao.getPlayersByTeamId(teamId).first().size)
        }
    }
}

class TeamPlayersViewModelFactory(
    private val teamId: String,
    private val playerDao: PlayerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamPlayersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeamPlayersViewModel(teamId, playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

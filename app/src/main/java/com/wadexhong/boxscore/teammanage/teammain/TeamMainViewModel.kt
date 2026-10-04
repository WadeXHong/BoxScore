package com.wadexhong.boxscore.teammanage.teammain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.objects.TeamDetail
import com.wadexhong.boxscore.objects.TeamInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.wadexhong.boxscore.sync.TeamSync
import kotlinx.coroutines.launch

data class TeamMainUiState(
    val teams: List<TeamInfo> = emptyList(),
    val isLoading: Boolean = false
)

class TeamMainViewModel(
    private val teamDao: TeamDao,
    private val sync: TeamSync = TeamSync.Firebase
) : ViewModel() {

    val uiState: StateFlow<TeamMainUiState> = teamDao.getAllTeams()
        .map { entities ->
            val teamInfos = entities.map { entity ->
                val detail = TeamDetail(
                    teamName = entity.teamName,
                    teamId = entity.teamId,
                    teamPlayersAmount = entity.playersAmount,
                    teamHistoryAmount = entity.historyAmount
                )
                TeamInfo(
                    teamId = entity.teamId,
                    teamName = entity.teamName,
                    teamDetails = listOf(detail)
                )
            }
            TeamMainUiState(teams = teamInfos)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TeamMainUiState(isLoading = true)
        )

    init {
        // Earlier builds never updated players_amount when players were added or removed.
        viewModelScope.launch { teamDao.syncAllTeamPlayersAmounts() }
    }

    fun deleteTeam(teamId: String) {
        viewModelScope.launch {
            teamDao.deleteTeamWithPlayers(teamId)
            sync.teamDeleted(teamId)
        }
    }
}

// Factory to inject DAO since we skipped Hilt
class TeamMainViewModelFactory(private val teamDao: TeamDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamMainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeamMainViewModel(teamDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

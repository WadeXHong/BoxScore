package com.wadexhong.boxscore.teammanage.createteam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

data class CreateTeamUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CreateTeamViewModel(
    private val teamDao: TeamDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTeamUiState())
    val uiState: StateFlow<CreateTeamUiState> = _uiState.asStateFlow()

    fun createTeam(teamName: String) {
        val trimmedName = teamName.trim()
        if (trimmedName.isEmpty()) {
            _uiState.value = CreateTeamUiState(errorMessage = "不可空白或名稱重複")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateTeamUiState(isLoading = true)

            // Explicitly check for duplicate team names to replicate legacy behavior
            val existingTeams = teamDao.getAllTeams().first()
            if (existingTeams.any { it.teamName == trimmedName }) {
                _uiState.value = CreateTeamUiState(errorMessage = "不可空白或名稱重複")
                return@launch
            }

            val teamEntity = TeamInfoEntity(
                teamId = UUID.randomUUID().toString(),
                teamName = trimmedName,
                playersAmount = 0,
                historyAmount = 0
            )

            try {
                teamDao.insertTeam(teamEntity)
                _uiState.value = CreateTeamUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = CreateTeamUiState(errorMessage = "新增球隊失敗")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class CreateTeamViewModelFactory(
    private val teamDao: TeamDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTeamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateTeamViewModel(teamDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

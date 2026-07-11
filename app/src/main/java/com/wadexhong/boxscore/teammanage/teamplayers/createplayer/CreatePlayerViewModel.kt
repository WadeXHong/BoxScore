package com.wadexhong.boxscore.teammanage.teamplayers.createplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

data class CreatePlayerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class CreatePlayerViewModel(
    private val teamId: String,
    private val playerDao: PlayerDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePlayerUiState())
    val uiState: StateFlow<CreatePlayerUiState> = _uiState.asStateFlow()

    fun createPlayer(
        name: String,
        number: String,
        isC: Boolean,
        isPF: Boolean,
        isSF: Boolean,
        isSG: Boolean,
        isPG: Boolean
    ) {
        if (name.isBlank() || number.isBlank()) {
            _uiState.value = CreatePlayerUiState(errorMessage = "必須輸入名字與背號")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreatePlayerUiState(isLoading = true)
            
            // Check if number already exists
            val existingPlayers = playerDao.getPlayersByTeamId(teamId).first()
            if (existingPlayers.any { it.playerNumber == number }) {
                _uiState.value = CreatePlayerUiState(errorMessage = "背號不可重複")
                return@launch
            }
            
            // Check max 15 players limit
            if (existingPlayers.size >= 15) {
                _uiState.value = CreatePlayerUiState(errorMessage = "單場登入球員最多為15人")
                return@launch
            }

            val playerEntity = TeamPlayerEntity(
                playerId = UUID.randomUUID().toString(),
                teamId = teamId,
                playerNumber = number,
                playerName = name,
                center = if (isC) 1 else 0,
                powerForward = if (isPF) 1 else 0,
                smallForward = if (isSF) 1 else 0,
                shootingGuard = if (isSG) 1 else 0,
                pointGuard = if (isPG) 1 else 0
            )
            
            playerDao.insertPlayer(playerEntity)
            _uiState.value = CreatePlayerUiState(isSuccess = true)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class CreatePlayerViewModelFactory(
    private val teamId: String,
    private val playerDao: PlayerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreatePlayerViewModel(teamId, playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.wadexhong.boxscore.startgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class GameInfoBuilder(
    val isLoading: Boolean = true,
    val teams: List<TeamInfoEntity> = emptyList(),
    val selectedTeam: TeamInfoEntity? = null,
    val gameTitle: String = "",
    val opponentName: String = "",
    val gameDate: String = "",
    
    val players: List<TeamPlayerEntity> = emptyList(),
    val selectedPlayerIds: Set<String> = emptySet(),
    
    val quarterLength: Int = 10,
    val totalQuarters: Int = 4,
    val maxFouls: Int = 5,
    val timeoutsFirstHalf: Int = 5,
    val timeoutsSecondHalf: Int = 5,
    
    val isStep1Valid: Boolean = true,
    val isStep2Valid: Boolean = true,
    val isStep1Error: Boolean = false,
    val isStep2Error: Boolean = false
)

class StartGameViewModel(
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameInfoBuilder())
    val uiState: StateFlow<GameInfoBuilder> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            teamDao.getAllTeams().collectLatest { teamList ->
                _uiState.update { it.copy(
                    teams = teamList, 
                    selectedTeam = it.selectedTeam ?: teamList.firstOrNull(),
                    isLoading = false
                ) }
                teamList.firstOrNull()?.let { fetchPlayers(it.teamId) }
            }
        }
    }

    fun onTeamSelected(team: TeamInfoEntity) {
        _uiState.update { it.copy(selectedTeam = team, selectedPlayerIds = emptySet()) }
        fetchPlayers(team.teamId)
    }

    private fun fetchPlayers(teamId: String) {
        viewModelScope.launch {
            playerDao.getPlayersByTeamId(teamId).collectLatest { playerList ->
                _uiState.update { it.copy(players = playerList) }
            }
        }
    }

    fun onGameTitleChanged(title: String) {
        if (title.length <= 20) {
            _uiState.update { it.copy(gameTitle = title) }
        }
    }

    fun onOpponentNameChanged(name: String) {
        if (name.length <= 20) {
            _uiState.update { it.copy(opponentName = name, isStep1Error = false) }
        }
    }

    fun onGameDateChanged(date: String) {
        _uiState.update { it.copy(gameDate = date) }
    }

    fun onPlayerSelected(playerId: String, isSelected: Boolean) {
        _uiState.update { state ->
            val newSelection = if (isSelected) {
                state.selectedPlayerIds + playerId
            } else {
                state.selectedPlayerIds - playerId
            }
            state.copy(selectedPlayerIds = newSelection, isStep2Error = false)
        }
    }

    fun onQuarterLengthChanged(length: Int) {
        if (length in 1..99) _uiState.update { it.copy(quarterLength = length) }
    }

    fun onTotalQuartersChanged(quarters: Int) {
        if (quarters in 1..9) _uiState.update { it.copy(totalQuarters = quarters) }
    }

    fun onMaxFoulsChanged(fouls: Int) {
        if (fouls in 1..9) _uiState.update { it.copy(maxFouls = fouls) }
    }

    fun onTimeoutsFirstHalfChanged(timeouts: Int) {
        if (timeouts in 0..9) _uiState.update { it.copy(timeoutsFirstHalf = timeouts) }
    }

    fun onTimeoutsSecondHalfChanged(timeouts: Int) {
        if (timeouts in 0..9) _uiState.update { it.copy(timeoutsSecondHalf = timeouts) }
    }

    fun validateStep1(): Boolean {
        val isValid = _uiState.value.opponentName.isNotBlank() && _uiState.value.selectedTeam != null
        _uiState.update { it.copy(isStep1Valid = isValid, isStep1Error = !isValid) }
        return isValid
    }

    fun validateStep2(): Boolean {
        val isValid = _uiState.value.selectedPlayerIds.size >= 6
        _uiState.update { it.copy(isStep2Valid = isValid, isStep2Error = !isValid) }
        return isValid
    }

    fun buildGameInfo(): GameInfo {
        val state = _uiState.value
        val gameInfo = GameInfo()
        gameInfo.gameId = UUID.randomUUID().toString()
        gameInfo.gameName = state.gameTitle
        gameInfo.opponentName = state.opponentName
        gameInfo.gameDate = state.gameDate
        gameInfo.yourTeam = state.selectedTeam?.teamName ?: ""
        gameInfo.yourTeamId = state.selectedTeam?.teamId ?: ""
        
        gameInfo.quarterLength = state.quarterLength.toString()
        gameInfo.totalQuarter = state.totalQuarters.toString()
        gameInfo.maxFoul = state.maxFouls.toString()
        gameInfo.timeoutFirstHalf = state.timeoutsFirstHalf.toString()
        gameInfo.timeoutSecondHalf = state.timeoutsSecondHalf.toString()
        
        val selectedPlayers = state.players.filter { state.selectedPlayerIds.contains(it.playerId) }
            .map { Player(it.playerNumber, it.playerName, it.playerId) }
        
        val starters = ArrayList(selectedPlayers.take(5))
        val subs = ArrayList(selectedPlayers.drop(5))
        
        gameInfo.startingPlayerList = starters
        gameInfo.substitutePlayerList = subs
        
        return gameInfo
    }
}

class StartGameViewModelFactory(
    private val teamDao: TeamDao,
    private val playerDao: PlayerDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StartGameViewModel(teamDao, playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

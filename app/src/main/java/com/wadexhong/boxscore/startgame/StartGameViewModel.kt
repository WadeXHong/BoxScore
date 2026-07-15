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
    val gameDate: String = java.text.SimpleDateFormat("yyyy年MM月dd日", java.util.Locale.getDefault()).format(java.util.Calendar.getInstance().time),
    
    val players: List<TeamPlayerEntity> = emptyList(),
    val startingPlayerIds: List<String> = emptyList(),
    val substitutePlayerIds: List<String> = emptyList(),
    
    val quarterLength: Int = 10,
    val totalQuarters: Int = 4,
    val maxFouls: Int = 5,
    val timeoutsFirstHalf: Int = 5,
    val timeoutsSecondHalf: Int = 5,
    
    val isStep1Valid: Boolean = false,
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
        _uiState.update { it.copy(selectedTeam = team, startingPlayerIds = emptyList(), substitutePlayerIds = emptyList()) }
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

    fun moveToStarting(playerId: String) {
        _uiState.update { state ->
            if (state.startingPlayerIds.size >= 5) return@update state
            val newStarters = state.startingPlayerIds + playerId
            val newSubs = state.substitutePlayerIds - playerId
            state.copy(startingPlayerIds = newStarters, substitutePlayerIds = newSubs, isStep2Error = false)
        }
    }

    fun moveToSubstitute(playerId: String) {
        _uiState.update { state ->
            if (state.startingPlayerIds.size + state.substitutePlayerIds.size >= 15) return@update state
            val newSubs = state.substitutePlayerIds + playerId
            val newStarters = state.startingPlayerIds - playerId
            state.copy(startingPlayerIds = newStarters, substitutePlayerIds = newSubs, isStep2Error = false)
        }
    }

    fun removeFromRoster(playerId: String) {
        _uiState.update { state ->
            state.copy(
                startingPlayerIds = state.startingPlayerIds - playerId,
                substitutePlayerIds = state.substitutePlayerIds - playerId,
                isStep2Error = false
            )
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
        val isValid = _uiState.value.startingPlayerIds.size == 5 && _uiState.value.substitutePlayerIds.isNotEmpty()
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
        
        val starters = state.players.filter { state.startingPlayerIds.contains(it.playerId) }.map {
            com.wadexhong.boxscore.objects.Player(it.playerNumber, it.playerName, it.playerId)
        }
        val subs = state.players.filter { state.substitutePlayerIds.contains(it.playerId) }.map {
            com.wadexhong.boxscore.objects.Player(it.playerNumber, it.playerName, it.playerId)
        }
        gameInfo.startingPlayerList = java.util.ArrayList(starters)
        gameInfo.substitutePlayerList = java.util.ArrayList(subs)
        
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

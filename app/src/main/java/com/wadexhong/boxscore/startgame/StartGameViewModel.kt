package com.wadexhong.boxscore.startgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.database.dao.PlayerDao
import com.wadexhong.boxscore.database.dao.TeamDao
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/** A toast for a stepper that hit its bound: string resource with one %d argument. */
data class LimitMessage(val messageRes: Int, val limit: Int)

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

    companion object {
        // Bounds from legacy DetailSettingPresenter.
        const val MIN_QUARTER_LENGTH = 1
        const val MAX_QUARTER_LENGTH = 40
        const val MIN_TOTAL_QUARTER = 1
        const val MAX_TOTAL_QUARTER = 4
        const val MIN_FOUL = 1
        const val MAX_FOUL = 9
        const val MIN_TIMEOUT = 1
        const val MAX_TIMEOUT = 9

        /** Legacy only offers teams that can field 5 starters plus at least 1 substitute. */
        const val MIN_TEAM_PLAYERS = 6
    }

    private val _uiState = MutableStateFlow(GameInfoBuilder())
    val uiState: StateFlow<GameInfoBuilder> = _uiState.asStateFlow()

    private val _limitMessages = MutableSharedFlow<LimitMessage>(extraBufferCapacity = 1)
    val limitMessages: SharedFlow<LimitMessage> = _limitMessages.asSharedFlow()

    private var playersJob: Job? = null

    init {
        viewModelScope.launch {
            // Count real roster rows: the stored playersAmount is not kept in sync by TeamManage.
            combine(teamDao.getAllTeams(), playerDao.getAllPlayers()) { allTeams, allPlayers ->
                val counts = allPlayers.groupingBy { it.teamId }.eachCount()
                allTeams.filter { (counts[it.teamId] ?: 0) >= MIN_TEAM_PLAYERS }
            }.collectLatest { teamList ->
                val previousTeamId = _uiState.value.selectedTeam?.teamId
                _uiState.update { it.copy(
                    teams = teamList, 
                    selectedTeam = it.selectedTeam?.takeIf { selected -> teamList.any { t -> t.teamId == selected.teamId } }
                        ?: teamList.firstOrNull(),
                    isLoading = false
                ) }
                val selectedTeamId = _uiState.value.selectedTeam?.teamId
                if (selectedTeamId != null && (selectedTeamId != previousTeamId || playersJob == null)) {
                    fetchPlayers(selectedTeamId)
                }
            }
        }
    }

    fun onTeamSelected(team: TeamInfoEntity) {
        _uiState.update { it.copy(selectedTeam = team, startingPlayerIds = emptyList(), substitutePlayerIds = emptyList()) }
        fetchPlayers(team.teamId)
    }

    private fun fetchPlayers(teamId: String) {
        playersJob?.cancel()
        playersJob = viewModelScope.launch {
            playerDao.getPlayersByTeamId(teamId).collectLatest { playerList ->
                _uiState.update { it.copy(players = playerList) }
            }
        }
    }

    fun onGameTitleChanged(title: String) {
        val input = title.trimStart(' ')
        if (input.length <= 20) {
            _uiState.update { it.copy(gameTitle = input) }
        }
    }

    fun onOpponentNameChanged(name: String) {
        val input = name.trimStart(' ')
        if (input.length <= 20) {
            _uiState.update { it.copy(opponentName = input, isStep1Error = false) }
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

    /**
     * Applies [value] when it is within [min]..[max]; otherwise emits the legacy toast for the
     * bound that was hit and keeps the current value.
     */
    private fun stepTo(value: Int, min: Int, max: Int, minRes: Int, maxRes: Int, apply: (Int) -> Unit) {
        when {
            value > max -> _limitMessages.tryEmit(LimitMessage(maxRes, max))
            value < min -> _limitMessages.tryEmit(LimitMessage(minRes, min))
            else -> apply(value)
        }
    }

    fun onQuarterLengthChanged(length: Int) = stepTo(
        length, MIN_QUARTER_LENGTH, MAX_QUARTER_LENGTH,
        R.string.min_quarter_length_message, R.string.max_quarter_length_message
    ) { v -> _uiState.update { it.copy(quarterLength = v) } }

    fun onTotalQuartersChanged(quarters: Int) = stepTo(
        quarters, MIN_TOTAL_QUARTER, MAX_TOTAL_QUARTER,
        R.string.min_total_quarter_message, R.string.max_total_quarter_message
    ) { v -> _uiState.update { it.copy(totalQuarters = v) } }

    fun onMaxFoulsChanged(fouls: Int) = stepTo(
        fouls, MIN_FOUL, MAX_FOUL,
        R.string.min_foul_message, R.string.max_foul_message
    ) { v -> _uiState.update { it.copy(maxFouls = v) } }

    fun onTimeoutsFirstHalfChanged(timeouts: Int) = stepTo(
        timeouts, MIN_TIMEOUT, MAX_TIMEOUT,
        R.string.min_timeout_first_half, R.string.max_timeout_first_half
    ) { v -> _uiState.update { it.copy(timeoutsFirstHalf = v) } }

    fun onTimeoutsSecondHalfChanged(timeouts: Int) = stepTo(
        timeouts, MIN_TIMEOUT, MAX_TIMEOUT,
        R.string.min_timeout_second_half, R.string.max_timeout_second_half
    ) { v -> _uiState.update { it.copy(timeoutsSecondHalf = v) } }

    /** Validates one wizard page; pages after the player list have nothing to check. */
    fun validatePage(page: Int): Boolean = when (page) {
        0 -> validateStep1()
        1 -> validateStep2()
        else -> true
    }

    /** Index of the first page before [targetPage] whose input is invalid, or -1 if all pass. */
    fun firstInvalidPageBefore(targetPage: Int): Int =
        (0 until targetPage).firstOrNull { !validatePage(it) } ?: -1

    fun validateStep1(): Boolean {
        val isValid = _uiState.value.opponentName.isNotBlank()
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
        // Legacy falls back to the "quick game" team when no team is selected.
        val context = com.wadexhong.boxscore.BoxScore.getAppContext()
        gameInfo.yourTeam = state.selectedTeam?.teamName ?: context.getString(R.string.quick_game_team_name)
        gameInfo.yourTeamId = state.selectedTeam?.teamId ?: context.getString(R.string.quick_game_team_id)
        
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

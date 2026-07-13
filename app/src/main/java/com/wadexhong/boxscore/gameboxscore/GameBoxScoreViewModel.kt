package com.wadexhong.boxscore.gameboxscore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GameUiState(
    val gameId: String = "",
    val yourScore: Int = 0,
    val yourFouls: Int = 0,
    val opponentScore: Int = 0,
    val opponentFouls: Int = 0,
    val quarter: Int = 1,
    val playersOnCourt: List<TeamPlayerEntity> = emptyList(),
    val benchPlayers: List<TeamPlayerEntity> = emptyList(),
    val actionHistory: List<GameEventEntity> = emptyList()
)

class GameBoxScoreViewModel(
    private val database: BoxScoreDatabase,
    private val gameId: String,
    private val teamId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(gameId = gameId))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        // Load events
        viewModelScope.launch {
            database.gameEventDao().getEventsByGameId(gameId).collect { events ->
                recalculateState(events)
            }
        }
        
        // Load players and simple substitution logic
        viewModelScope.launch {
            database.playerDao().getPlayersByTeamId(teamId).collect { players ->
                // Basic stub: first 5 on court, rest on bench. In reality this needs an actual substitution log or boolean field.
                val onCourt = players.take(5)
                val bench = players.drop(5)
                _uiState.update { it.copy(playersOnCourt = onCourt, benchPlayers = bench) }
            }
        }
    }

    private fun recalculateState(events: List<GameEventEntity>) {
        var yScore = 0
        var yFouls = 0
        var oScore = 0
        var oFouls = 0
        var currQuarter = 1

        for (event in events) {
            when (event.actionType) {
                RecordDataType.TWO_POINT_SHOT_MADE -> yScore += 2
                RecordDataType.THREE_POINT_SHOT_MADE -> yScore += 3
                RecordDataType.FREE_THROW_SHOT_MADE -> yScore += 1
                RecordDataType.FOUL, RecordDataType.OFFENSIVE_FOUL, RecordDataType.DEFENSIVE_FOUL -> yFouls += 1
                RecordDataType.OPPONENT_TEAM_TOTAL_SCORE -> oScore += 1 // We assume +1, or maybe we have +2/+3 as separate events, but legacy had basic buttons
                RecordDataType.OPPONENT_TEAM_FOUL -> oFouls += 1
                RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_SCORE -> oScore -= 1
                RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_FOUL -> oFouls -= 1
                RecordDataType.QUARTER -> currQuarter += 1
                RecordDataType.MINUS_QUARTER -> currQuarter -= 1
            }
        }
        _uiState.update { it.copy(
            yourScore = yScore,
            yourFouls = yFouls,
            opponentScore = oScore,
            opponentFouls = oFouls,
            quarter = currQuarter,
            actionHistory = events
        )}
    }

    fun onAction(actionType: Int, playerId: String? = null) {
        viewModelScope.launch {
            val event = GameEventEntity(
                gameId = gameId,
                actionType = actionType,
                playerId = playerId,
                quarter = _uiState.value.quarter
            )
            database.gameEventDao().insertEvent(event)
        }
    }

    fun undoAction(eventId: Long) {
        viewModelScope.launch {
            database.gameEventDao().deleteEventById(eventId)
        }
    }
}

class GameBoxScoreViewModelFactory(
    private val database: BoxScoreDatabase,
    private val gameId: String,
    private val teamId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameBoxScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameBoxScoreViewModel(database, gameId, teamId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.wadexhong.boxscore.gameboxscore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.BoxScoreDatabase
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import com.wadexhong.boxscore.objects.GameInfo
import com.wadexhong.boxscore.objects.Player
import com.wadexhong.boxscore.setting.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GameUiState(
    val gameId: String = "",
    val yourScore: Int = 0,
    val yourFouls: Int = 0,
    val opponentScore: Int = 0,
    val opponentFouls: Int = 0,
    val quarter: Int = 1,
    val onCourtPlayers: List<TeamPlayerEntity> = emptyList(),
    val benchPlayers: List<TeamPlayerEntity> = emptyList(),
    /** Player stat events only (what the legacy undo list held), in chronological order. */
    val actionHistory: List<GameEventEntity> = emptyList()
)

/**
 * Event types that only exist in the Room event log. Substitutions are stored as an OUT/IN pair
 * so the lineup can be rebuilt after process death without a schema change.
 */
object GameEventType {
    const val SUBSTITUTION_OUT = 0x2001
    const val SUBSTITUTION_IN = 0x2002
}

data class SwipeGesture(val fingerCount: Int, val direction: SwipeDirection)

class GameBoxScoreViewModel(
    private val database: BoxScoreDatabase,
    private val gameId: String,
    private val teamId: String,
    private val gameInfo: GameInfo? = null,
    settingsRepository: SettingsRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(gameId = gameId))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val gestureBindings: StateFlow<Map<SwipeGesture, Int>> =
        settingsRepository?.let { repo ->
            combine(
                repo.doubleUpFlow, repo.doubleDownFlow, repo.doubleLeftFlow, repo.doubleRightFlow,
                repo.tripleUpFlow, repo.tripleDownFlow, repo.tripleLeftFlow, repo.tripleRightFlow
            ) { values ->
                val keys = listOf(2, 3).flatMap { fingers ->
                    listOf(SwipeDirection.UP, SwipeDirection.DOWN, SwipeDirection.LEFT, SwipeDirection.RIGHT)
                        .map { SwipeGesture(fingers, it) }
                }
                keys.zip(values.toList()).toMap()
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())
        } ?: MutableStateFlow(emptyMap())

    init {
        viewModelScope.launch {
            combine(database.gameEventDao().getEventsByGameId(gameId), rosterFlow()) { events, roster ->
                events to roster
            }.collect { (events, roster) -> recalculateState(events, roster) }
        }
    }

    /** Players from the Start Game wizard when available, otherwise the team roster in Room. */
    private fun rosterFlow(): Flow<List<TeamPlayerEntity>> {
        val starting = gameInfo?.startingPlayerList
        if (starting.isNullOrEmpty()) return database.playerDao().getPlayersByTeamId(teamId)
        val substitutes = gameInfo?.substitutePlayerList ?: emptyList()
        return flowOf((starting + substitutes).map { it.toEntity() })
    }

    private fun Player.toEntity() = TeamPlayerEntity(
        teamId = teamId, playerId = playerId ?: "", playerName = name ?: "", playerNumber = number ?: "",
        center = 0, powerForward = 0, smallForward = 0, shootingGuard = 0, pointGuard = 0
    )

    private fun startingPlayerIds(roster: List<TeamPlayerEntity>): List<String> {
        val starting = gameInfo?.startingPlayerList
        return if (starting.isNullOrEmpty()) roster.take(5).map { it.playerId }
        else starting.map { it.playerId ?: "" }
    }

    private fun recalculateState(events: List<GameEventEntity>, roster: List<TeamPlayerEntity>) {
        val snapshot = GameStateCalculator.calculate(events, startingPlayerIds(roster))
        val rosterById = roster.associateBy { it.playerId }

        _uiState.update {
            it.copy(
                yourScore = snapshot.yourScore,
                yourFouls = snapshot.yourFouls,
                opponentScore = snapshot.opponentScore,
                opponentFouls = snapshot.opponentFouls,
                quarter = snapshot.quarter,
                onCourtPlayers = snapshot.onCourtIds.mapNotNull { id -> rosterById[id] },
                benchPlayers = roster.filter { p -> p.playerId !in snapshot.onCourtIds },
                actionHistory = snapshot.history
            )
        }
    }

    private val maxFoul: Int get() = gameInfo?.maxFoul?.toIntOrNull() ?: 5

    fun onOpponentScoreChanged(scoreDelta: Int) {
        if (scoreDelta < 0 && _uiState.value.opponentScore <= 0) return
        val actionType = if (scoreDelta > 0) RecordDataType.OPPONENT_TEAM_TOTAL_SCORE
        else RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_SCORE
        repeat(Math.abs(scoreDelta)) { onStatRecorded(actionType, null) }
    }

    fun onOpponentFoulChanged(foulDelta: Int) {
        if (foulDelta > 0 && _uiState.value.opponentFouls >= maxFoul) return
        if (foulDelta < 0 && _uiState.value.opponentFouls <= 0) return
        val actionType = if (foulDelta > 0) RecordDataType.OPPONENT_TEAM_FOUL
        else RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_FOUL
        repeat(Math.abs(foulDelta)) { onStatRecorded(actionType, null) }
    }

    fun onYourFoulChanged(foulDelta: Int) {
        if (foulDelta > 0 && _uiState.value.yourFouls >= maxFoul) return
        if (foulDelta < 0 && _uiState.value.yourFouls <= 0) return
        val actionType = if (foulDelta > 0) RecordDataType.YOUR_TEAM_FOUL
        else RecordDataType.MINUS_YOUR_TEAM_TEAM_FOUL
        repeat(Math.abs(foulDelta)) { onStatRecorded(actionType, null) }
    }

    fun advanceQuarter() {
        val totalQuarters = gameInfo?.totalQuarter?.toIntOrNull() ?: 4
        if (_uiState.value.quarter < totalQuarters) {
            onStatRecorded(RecordDataType.QUARTER, null)
        }
    }

    fun onStatRecorded(actionType: Int, playerId: String? = null) {
        insert(actionType, playerId, _uiState.value.quarter)
    }

    /** Retroactively records a stat in an earlier quarter (legacy "補登"). */
    fun onMakeUp(playerId: String, actionType: Int, quarter: Int) {
        insert(actionType, playerId, quarter)
    }

    private fun insert(actionType: Int, playerId: String?, quarter: Int) {
        viewModelScope.launch {
            database.gameEventDao().insertEvent(
                GameEventEntity(gameId = gameId, actionType = actionType, playerId = playerId, quarter = quarter)
            )
        }
    }

    /** Returns the stat bound to this swipe in Settings, or -1 when none is set. */
    fun actionForGesture(gesture: SwipeGesture): Int = gestureBindings.value[gesture] ?: -1

    fun onPlayerSubstituted(courtPlayerId: String, benchPlayerId: String) {
        val state = _uiState.value
        if (state.onCourtPlayers.none { it.playerId == courtPlayerId }) return
        if (state.benchPlayers.none { it.playerId == benchPlayerId }) return

        viewModelScope.launch {
            database.gameEventDao().insertEvents(
                listOf(
                    GameEventEntity(gameId = gameId, actionType = GameEventType.SUBSTITUTION_OUT, playerId = courtPlayerId, quarter = state.quarter),
                    GameEventEntity(gameId = gameId, actionType = GameEventType.SUBSTITUTION_IN, playerId = benchPlayerId, quarter = state.quarter)
                )
            )
        }
    }

    /** Changes the player and/or stat type of a recorded event, keeping its quarter and position. */
    fun onEditEvent(event: GameEventEntity, playerId: String, actionType: Int) {
        viewModelScope.launch {
            database.gameEventDao().updateEvent(event.copy(playerId = playerId, actionType = actionType))
        }
    }

    fun onUndoAction(eventId: Long) {
        viewModelScope.launch {
            database.gameEventDao().deleteEventById(eventId)
        }
    }
}

class GameBoxScoreViewModelFactory(
    private val database: BoxScoreDatabase,
    private val gameId: String,
    private val teamId: String,
    private val gameInfo: GameInfo? = null,
    private val settingsRepository: SettingsRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameBoxScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameBoxScoreViewModel(database, gameId, teamId, gameInfo, settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.wadexhong.boxscore.gamehistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wadexhong.boxscore.database.GameRepository
import com.wadexhong.boxscore.database.dao.GameDao
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/** One-off results of the "更多" menu actions. */
sealed interface HistoryEvent {
    /** The uploaded .xls URL, to put in the clipboard (legacy saveUrlInClipboard). */
    data class Shared(val url: String) : HistoryEvent
    data object ShareFailed : HistoryEvent
    data object CannotCreateExcel : HistoryEvent
}

/** Uploads an exported file and returns its download URL; null on failure. */
fun interface ExcelUploader {
    suspend fun upload(gameId: String, file: File): String?
}

class GameHistoryListViewModel(
    private val gameDao: GameDao,
    private val repository: GameRepository,
    private val exportDirectory: File,
    private val uploader: ExcelUploader,
    /** Called after a finished game is deleted locally, with the team's new history count. */
    private val onGameDeleted: (teamId: String, gameId: String, historyAmount: Int) -> Unit = { _, _, _ -> },
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val games: StateFlow<List<GameInfoEntity>> = gameDao.getFinishedGames()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _events = MutableSharedFlow<HistoryEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<HistoryEvent> = _events.asSharedFlow()

    fun deleteGame(game: GameInfoEntity) {
        viewModelScope.launch {
            val amount = repository.deleteFinishedGame(game.gameId, game.yourTeamId)
            onGameDeleted(game.yourTeamId, game.gameId, amount)
        }
    }

    /** Legacy createAndShareGameHistoryXls: export to .xls, upload, hand back the URL. */
    fun shareGame(game: GameInfoEntity) {
        if (_isUploading.value) return
        _isUploading.value = true
        viewModelScope.launch {
            val rows = gameDao.getGameDataList(game.gameId)
            val file = withContext(ioDispatcher) {
                runCatching { GameExcelExporter.export(exportDirectory, game, rows) }.getOrNull()
            }
            val event = if (file == null) {
                HistoryEvent.CannotCreateExcel
            } else {
                uploader.upload(game.gameId, file)?.let { HistoryEvent.Shared(it) } ?: HistoryEvent.ShareFailed
            }
            _isUploading.value = false
            _events.emit(event)
        }
    }
}

data class HistoryDetailUiState(
    val info: GameInfoEntity? = null,
    val rows: List<GameDataEntity> = emptyList(),
    /** Players table filter: [HistoryStats.FILTER_TOTAL] or a quarter 1..4. */
    val quarterFilter: Int = HistoryStats.FILTER_TOTAL
) {
    val teamByQuarter: List<GameDataEntity> get() = HistoryStats.teamByQuarter(rows)
    val teamTotal: GameDataEntity get() = HistoryStats.sum(rows)
    val players: List<GameDataEntity> get() = HistoryStats.playersFor(rows, quarterFilter)
}

class HistoryDetailViewModel(
    private val gameDao: GameDao,
    private val gameId: String
) : ViewModel() {

    private val quarterFilter = MutableStateFlow(HistoryStats.FILTER_TOTAL)
    private val info = MutableStateFlow<GameInfoEntity?>(null)

    val uiState: StateFlow<HistoryDetailUiState> =
        combine(info, gameDao.getGameDataByGameId(gameId), quarterFilter) { info, rows, filter ->
            HistoryDetailUiState(info, rows, filter)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HistoryDetailUiState())

    init {
        viewModelScope.launch { info.value = gameDao.getGameInfoById(gameId) }
    }

    fun onQuarterFilterSelected(filter: Int) {
        quarterFilter.value = filter
    }
}

class GameHistoryListViewModelFactory(
    private val gameDao: GameDao,
    private val repository: GameRepository,
    private val exportDirectory: File,
    private val uploader: ExcelUploader,
    private val onGameDeleted: (String, String, Int) -> Unit
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GameHistoryListViewModel(gameDao, repository, exportDirectory, uploader, onGameDeleted) as T
}

class HistoryDetailViewModelFactory(
    private val gameDao: GameDao,
    private val gameId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        HistoryDetailViewModel(gameDao, gameId) as T
}

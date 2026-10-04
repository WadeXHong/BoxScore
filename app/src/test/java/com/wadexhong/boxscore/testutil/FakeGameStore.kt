package com.wadexhong.boxscore.testutil

import com.wadexhong.boxscore.database.dao.GameDao
import com.wadexhong.boxscore.database.dao.GameEventDao
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory game_info / game_data / game_event tables with auto-increment ids. */
class FakeGameStore {
    val infos = MutableStateFlow<List<GameInfoEntity>>(emptyList())
    val data = MutableStateFlow<List<GameDataEntity>>(emptyList())
    val events = MutableStateFlow<List<GameEventEntity>>(emptyList())
    private var nextId = 1L

    val gameDao: GameDao = object : GameDao {
        override fun getAllGameInfos(): Flow<List<GameInfoEntity>> = infos.map { it.sortedByDescending { i -> i.id } }
        override suspend fun getGameInfoById(gameId: String) = infos.value.find { it.gameId == gameId }
        override suspend fun insertGameInfo(gameInfo: GameInfoEntity): Long {
            val row = gameInfo.copy(id = nextId++)
            infos.update { list -> list.filterNot { it.gameId == gameInfo.gameId } + row }
            return row.id
        }
        override suspend fun updateGameInfo(gameInfo: GameInfoEntity) {
            infos.update { list -> list.map { if (it.id == gameInfo.id) gameInfo else it } }
        }
        override suspend fun deleteGameInfoById(gameId: String) {
            infos.update { list -> list.filterNot { it.gameId == gameId } }
        }
        override suspend fun getUnfinishedGame() = infos.value.filter { it.isGameover == 0 }.maxByOrNull { it.id }
        override fun getFinishedGames(): Flow<List<GameInfoEntity>> = infos.map { list ->
            list.filter { it.isGameover == 1 }.sortedWith(compareByDescending<GameInfoEntity> { it.gameDate }.thenByDescending { it.id })
        }
        override suspend fun getFinishedGameIds() = infos.value.filter { it.isGameover == 1 }.map { it.gameId }
        override suspend fun countFinishedGames(teamId: String) =
            infos.value.count { it.yourTeamId == teamId && it.isGameover == 1 }

        override fun getGameDataByGameId(gameId: String): Flow<List<GameDataEntity>> =
            data.map { list -> list.filter { it.gameId == gameId }.sortedBy { it.id } }
        override suspend fun getGameDataList(gameId: String) = data.value.filter { it.gameId == gameId }.sortedBy { it.id }
        override suspend fun insertGameData(gameData: GameDataEntity): Long {
            val row = gameData.copy(id = nextId++)
            data.update { it + row }
            return row.id
        }
        override suspend fun insertAllGameData(gameDataList: List<GameDataEntity>) {
            gameDataList.forEach { insertGameData(it) }
        }
        override suspend fun updateGameData(gameData: GameDataEntity) {
            data.update { list -> list.map { if (it.id == gameData.id) gameData else it } }
        }
        override suspend fun deleteGameDataByGameId(gameId: String) {
            data.update { list -> list.filterNot { it.gameId == gameId } }
        }
    }

    val eventDao: GameEventDao = object : GameEventDao {
        override fun getEventsByGameId(gameId: String) = events.map { list -> list.filter { it.gameId == gameId } }
        override suspend fun getEventsList(gameId: String) = events.value.filter { it.gameId == gameId }
        override suspend fun insertEvent(event: GameEventEntity): Long {
            val row = event.copy(id = nextId++)
            events.update { it + row }
            return row.id
        }
        override suspend fun insertEvents(events: List<GameEventEntity>) {
            events.forEach { insertEvent(it) }
        }
        override suspend fun updateEvent(event: GameEventEntity) {
            events.update { list -> list.map { if (it.id == event.id) event else it } }
        }
        override suspend fun deleteEventById(eventId: Long) {
            events.update { list -> list.filterNot { it.id == eventId } }
        }
        override suspend fun deleteEventsByGameId(gameId: String) {
            events.update { list -> list.filterNot { it.gameId == gameId } }
        }
    }
}

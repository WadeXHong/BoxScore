package com.wadexhong.boxscore.sync

import com.wadexhong.boxscore.Constants.FireBaseConstant
import com.wadexhong.boxscore.Constants.GameDataDBContract
import com.wadexhong.boxscore.Constants.GameInfoDBContract
import com.wadexhong.boxscore.Constants.TeamInfoDBContract
import com.wadexhong.boxscore.Constants.TeamPlayersContract
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameInfoEntity
import com.wadexhong.boxscore.database.entity.TeamInfoEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

/** A user's node (users/{uid}) parsed into Room entities. */
data class RemoteData(
    val teams: List<TeamInfoEntity> = emptyList(),
    val players: List<TeamPlayerEntity> = emptyList(),
    val games: List<RemoteGame> = emptyList()
)

data class RemoteGame(val info: GameInfoEntity, val rows: List<GameDataEntity>)

/**
 * Converts between Room entities and the Firebase Realtime Database layout written by the
 * legacy Create / read by OnCreateDataBaseTask. Pure functions, so they can be unit tested.
 */
object FirebaseMapper {

    /** Child values of game_info/{gameId}, without its game_data (legacy createGameInfo). */
    fun gameInfoValues(info: GameInfoEntity): Map<String, Any> = mapOf(
        GameInfoDBContract.COLUMN_NAME_GAME_ID to info.gameId,
        GameInfoDBContract.COLUMN_NAME_GAME_DATE to info.gameDate,
        GameInfoDBContract.COLUMN_NAME_GAME_NAME to info.gameName,
        GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER to info.isGameover,
        GameInfoDBContract.COLUMN_NAME_MAX_FOUL to info.maxFoul,
        GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME to info.opponentName,
        GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE to info.opponentScore,
        GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH to info.quarterLength,
        GameInfoDBContract.COLUMN_NAME_YOUR_TEAM to info.yourTeamName,
        GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF to info.timeoutFirstHalf,
        GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF to info.timeoutSecondHalf,
        GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER to info.totalQuarter,
        GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE to info.yourScore,
        GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID to info.yourTeamId
    )

    /**
     * Multi-path update, relative to users/{uid}, writing a finished game the way legacy
     * createGameData + createGameInfo + updateTeamHistoryAmount did.
     */
    fun gameUpload(info: GameInfoEntity, rows: List<GameDataEntity>, historyAmount: Int): Map<String, Any> {
        val gamePath = "${FireBaseConstant.NODE_NAME_GAME_INFO}/${info.gameId}"
        val updates = mutableMapOf<String, Any>()
        gameInfoValues(info).forEach { (key, value) -> updates["$gamePath/$key"] = value }
        rows.forEach { row ->
            val playerPath = "$gamePath/${FireBaseConstant.NODE_NAME_GAME_DATA}/${row.playerId}"
            updates["$playerPath/${GameDataDBContract.COLUMN_NAME_PLAYER_NAME}"] = row.playerName
            updates["$playerPath/${GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER}"] = row.playerNumber
            statValues(row).forEach { (key, value) ->
                updates["$playerPath/${FireBaseConstant.NODE_NAME_QUARTER}/${row.quarter}/$key"] = value
            }
        }
        updates[teamHistoryPath(info.yourTeamId)] = historyAmount
        return updates
    }

    fun teamValues(team: TeamInfoEntity): Map<String, Any> = mapOf(
        TeamInfoDBContract.COLUMN_NAME_TEAM_NAME to team.teamName,
        TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT to team.playersAmount,
        TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT to team.historyAmount
    )

    fun playerValues(player: TeamPlayerEntity): Map<String, Any> = mapOf(
        TeamPlayersContract.COLUMN_NAME_PLAYER_NAME to player.playerName,
        TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER to player.playerNumber,
        TeamPlayersContract.COLUMN_NAME_PLAY_C to player.center,
        TeamPlayersContract.COLUMN_NAME_PLAY_PF to player.powerForward,
        TeamPlayersContract.COLUMN_NAME_PLAY_SF to player.smallForward,
        TeamPlayersContract.COLUMN_NAME_PLAY_SG to player.shootingGuard,
        TeamPlayersContract.COLUMN_NAME_PLAY_PG to player.pointGuard,
        TeamPlayersContract.COLUMN_NAME_TEAM_ID to player.teamId
    )

    fun teamHistoryPath(teamId: String) =
        "${FireBaseConstant.NODE_NAME_TEAM_INFO}/$teamId/${TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT}"

    fun teamPlayersAmountPath(teamId: String) =
        "${FireBaseConstant.NODE_NAME_TEAM_INFO}/$teamId/${TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT}"

    private fun statValues(row: GameDataEntity): Map<String, Int> = mapOf(
        GameDataDBContract.COLUMN_NAME_POINTS to row.pts,
        GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE to row.fgm,
        GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED to row.fga,
        GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE to row.tpm,
        GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED to row.tpa,
        GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE to row.ftm,
        GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED to row.fta,
        GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND to row.oreb,
        GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND to row.dreb,
        GameDataDBContract.COLUMN_NAME_ASSIST to row.ast,
        GameDataDBContract.COLUMN_NAME_STEAL to row.stl,
        GameDataDBContract.COLUMN_NAME_BLOCK to row.blk,
        GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL to row.pf,
        GameDataDBContract.COLUMN_NAME_TURNOVER to row.tov
    )

    /** Parses DataSnapshot.getValue() of users/{uid} (legacy OnCreateDataBaseTask). */
    fun parseUser(value: Any?): RemoteData {
        val root = value as? Map<*, *> ?: return RemoteData()

        val teams = children(root[FireBaseConstant.NODE_NAME_TEAM_INFO]).map { (teamId, node) ->
            TeamInfoEntity(
                teamId = teamId,
                teamName = node.string(TeamInfoDBContract.COLUMN_NAME_TEAM_NAME),
                playersAmount = node.int(TeamInfoDBContract.COLUMN_NAME_TEAM_PLAYERS_AMOUNT),
                historyAmount = node.int(TeamInfoDBContract.COLUMN_NAME_TEAM_HISTORY_AMOUNT)
            )
        }

        val players = children(root[FireBaseConstant.NODE_NAME_TEAM_PLAYER]).map { (playerId, node) ->
            TeamPlayerEntity(
                teamId = node.string(TeamPlayersContract.COLUMN_NAME_TEAM_ID),
                playerId = playerId,
                playerName = node.string(TeamPlayersContract.COLUMN_NAME_PLAYER_NAME),
                playerNumber = node.string(TeamPlayersContract.COLUMN_NAME_PLAYER_NUMBER),
                center = node.int(TeamPlayersContract.COLUMN_NAME_PLAY_C),
                powerForward = node.int(TeamPlayersContract.COLUMN_NAME_PLAY_PF),
                smallForward = node.int(TeamPlayersContract.COLUMN_NAME_PLAY_SF),
                shootingGuard = node.int(TeamPlayersContract.COLUMN_NAME_PLAY_SG),
                pointGuard = node.int(TeamPlayersContract.COLUMN_NAME_PLAY_PG)
            )
        }

        val games = children(root[FireBaseConstant.NODE_NAME_GAME_INFO]).map { (gameId, node) ->
            val info = GameInfoEntity(
                gameId = gameId,
                gameName = node.string(GameInfoDBContract.COLUMN_NAME_GAME_NAME),
                yourTeamId = node.string(GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_ID),
                yourTeamName = node.string(GameInfoDBContract.COLUMN_NAME_YOUR_TEAM),
                opponentName = node.string(GameInfoDBContract.COLUMN_NAME_OPPONENT_NAME),
                quarterLength = node.int(GameInfoDBContract.COLUMN_NAME_QUARTER_LENGTH),
                totalQuarter = node.int(GameInfoDBContract.COLUMN_NAME_TOTAL_QUARTER),
                maxFoul = node.int(GameInfoDBContract.COLUMN_NAME_MAX_FOUL),
                timeoutFirstHalf = node.int(GameInfoDBContract.COLUMN_NAME_TIMEOUT_FIRST_HALF),
                timeoutSecondHalf = node.int(GameInfoDBContract.COLUMN_NAME_TIMEOUT_SECOND_HALF),
                yourScore = node.int(GameInfoDBContract.COLUMN_NAME_YOUR_TEAM_SCORE),
                opponentScore = node.int(GameInfoDBContract.COLUMN_NAME_OPPONENT_TEAM_SCORE),
                gameDate = node.string(GameInfoDBContract.COLUMN_NAME_GAME_DATE),
                isGameover = node.int(GameInfoDBContract.COLUMN_NAME_IS_GAMEOVER)
            )
            val rows = children(node[FireBaseConstant.NODE_NAME_GAME_DATA]).flatMap { (playerId, player) ->
                children(player[FireBaseConstant.NODE_NAME_QUARTER]).map { (quarter, q) ->
                    GameDataEntity(
                        gameId = gameId,
                        quarter = quarter.toIntOrNull() ?: 0,
                        playerId = playerId,
                        playerNumber = player.string(GameDataDBContract.COLUMN_NAME_PLAYER_NUMBER),
                        playerName = player.string(GameDataDBContract.COLUMN_NAME_PLAYER_NAME),
                        pts = q.int(GameDataDBContract.COLUMN_NAME_POINTS),
                        fgm = q.int(GameDataDBContract.COLUMN_NAME_FIELD_GOALS_MADE),
                        fga = q.int(GameDataDBContract.COLUMN_NAME_FIELD_GOALS_ATTEMPTED),
                        tpm = q.int(GameDataDBContract.COLUMN_NAME_THREE_POINT_MADE),
                        tpa = q.int(GameDataDBContract.COLUMN_NAME_THREE_POINT_ATTEMPTED),
                        ftm = q.int(GameDataDBContract.COLUMN_NAME_FREE_THROW_MADE),
                        fta = q.int(GameDataDBContract.COLUMN_NAME_FREE_THROW_ATTEMPTED),
                        oreb = q.int(GameDataDBContract.COLUMN_NAME_OFFENSIVE_REBOUND),
                        dreb = q.int(GameDataDBContract.COLUMN_NAME_DEFENSIVE_REBOUND),
                        ast = q.int(GameDataDBContract.COLUMN_NAME_ASSIST),
                        stl = q.int(GameDataDBContract.COLUMN_NAME_STEAL),
                        blk = q.int(GameDataDBContract.COLUMN_NAME_BLOCK),
                        pf = q.int(GameDataDBContract.COLUMN_NAME_PERSONAL_FOUL),
                        tov = q.int(GameDataDBContract.COLUMN_NAME_TURNOVER)
                    )
                }
            }.sortedWith(compareBy({ it.playerNumber.toIntOrNull() ?: Int.MAX_VALUE }, { it.playerId }, { it.quarter }))
            RemoteGame(info, rows)
        }

        return RemoteData(teams, players, games)
    }

    /**
     * Child nodes as (key, map). Firebase returns a node whose keys are small integers
     * (the quarters 1..4) as a List with nulls for missing indexes.
     */
    private fun children(node: Any?): List<Pair<String, Map<*, *>>> = when (node) {
        is Map<*, *> -> node.mapNotNull { (k, v) -> (v as? Map<*, *>)?.let { k.toString() to it } }
        is List<*> -> node.withIndex().mapNotNull { (i, v) -> (v as? Map<*, *>)?.let { i.toString() to it } }
        else -> emptyList()
    }

    private fun Map<*, *>.string(key: String): String = this[key]?.toString().orEmpty()

    private fun Map<*, *>.int(key: String): Int = when (val v = this[key]) {
        is Number -> v.toInt()
        is Boolean -> if (v) 1 else 0
        is String -> v.toIntOrNull() ?: 0
        else -> 0
    }
}

package com.wadexhong.boxscore.gameboxscore

import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity

/** Team-level state rebuilt from the event log. */
data class GameSnapshot(
    val yourScore: Int = 0,
    val yourFouls: Int = 0,
    val opponentScore: Int = 0,
    val opponentFouls: Int = 0,
    val quarter: Int = 1,
    val onCourtIds: List<String> = emptyList(),
    /** Player stat events only (what the legacy undo list held), in chronological order. */
    val history: List<GameEventEntity> = emptyList()
)

/**
 * Pure event-sourcing logic for a game, kept free of Android types so it can be unit tested.
 * The rules mirror legacy `GameDataDbHelper.plusData` / `minusData`.
 */
object GameStateCalculator {

    /** Legacy history and Firebase always store four quarter rows per player. */
    const val STORED_QUARTERS = 4

    /** Stat types a player can be credited with (legacy TYPE_CHOICE_INT plus the plain foul). */
    val PLAYER_STAT_TYPES: Set<Int> = setOf(
        RecordDataType.TWO_POINT_SHOT_MADE,
        RecordDataType.TWO_POINT_SHOT_MISSED,
        RecordDataType.THREE_POINT_SHOT_MADE,
        RecordDataType.THREE_POINT_SHOT_MISSED,
        RecordDataType.FREE_THROW_SHOT_MADE,
        RecordDataType.FREE_THROW_SHOT_MISSED,
        RecordDataType.OFFENSIVE_REBOUND,
        RecordDataType.DEFENSIVE_REBOUND,
        RecordDataType.ASSIST,
        RecordDataType.STEAL,
        RecordDataType.BLOCK,
        RecordDataType.FOUL,
        RecordDataType.OFFENSIVE_FOUL,
        RecordDataType.DEFENSIVE_FOUL,
        RecordDataType.TURNOVER
    )

    fun currentQuarter(events: List<GameEventEntity>): Int =
        1 + events.count { it.actionType == RecordDataType.QUARTER } -
            events.count { it.actionType == RecordDataType.MINUS_QUARTER }

    fun calculate(events: List<GameEventEntity>, startingIds: List<String>): GameSnapshot {
        var yourScore = 0
        var yourFouls = 0
        var opponentScore = 0
        var opponentFouls = 0
        val quarter = currentQuarter(events)

        val onCourtIds = startingIds.toMutableList()
        var pendingOutIndex = -1

        for (event in events) {
            when (event.actionType) {
                RecordDataType.TWO_POINT_SHOT_MADE -> yourScore += 2
                RecordDataType.THREE_POINT_SHOT_MADE -> yourScore += 3
                RecordDataType.FREE_THROW_SHOT_MADE -> yourScore += 1
                RecordDataType.OPPONENT_TEAM_TOTAL_SCORE -> opponentScore += 1
                RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_SCORE -> opponentScore -= 1
                GameEventType.SUBSTITUTION_OUT -> pendingOutIndex = onCourtIds.indexOf(event.playerId)
                GameEventType.SUBSTITUTION_IN -> {
                    val inId = event.playerId
                    if (pendingOutIndex >= 0 && inId != null && inId !in onCourtIds) {
                        onCourtIds[pendingOutIndex] = inId
                    }
                    pendingOutIndex = -1
                }
            }

            // Team fouls reset every quarter, so only the current quarter counts. Legacy only
            // charged defensive fouls (and the manual team-foul button) to the team.
            if (event.quarter == quarter) {
                when (event.actionType) {
                    RecordDataType.FOUL, RecordDataType.DEFENSIVE_FOUL,
                    RecordDataType.YOUR_TEAM_FOUL -> yourFouls += 1
                    RecordDataType.MINUS_FOUL, RecordDataType.MINUS_DEFENSIVE_FOUL,
                    RecordDataType.MINUS_YOUR_TEAM_TEAM_FOUL -> yourFouls -= 1
                    RecordDataType.OPPONENT_TEAM_FOUL -> opponentFouls += 1
                    RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_FOUL -> opponentFouls -= 1
                }
            }
        }

        return GameSnapshot(
            yourScore = yourScore,
            yourFouls = yourFouls,
            opponentScore = opponentScore,
            opponentFouls = opponentFouls,
            quarter = quarter,
            onCourtIds = onCourtIds,
            history = events.filter { it.playerId != null && it.actionType in PLAYER_STAT_TYPES }
        )
    }

    /**
     * Builds the legacy `game_data` rows (one per player per quarter) from the event log.
     * Players keep the order of [roster], which is how a resumed game finds its starters.
     */
    fun playerStats(
        gameId: String,
        events: List<GameEventEntity>,
        roster: List<TeamPlayerEntity>
    ): List<GameDataEntity> {
        val quarters = maxOf(STORED_QUARTERS, events.maxOfOrNull { it.quarter } ?: 0)
        return roster.flatMap { player ->
            (1..quarters).map { quarter ->
                val line = StatLine()
                events.filter { it.playerId == player.playerId && it.quarter == quarter }
                    .forEach { line.apply(it.actionType) }
                line.toEntity(gameId, quarter, player)
            }
        }
    }

    private class StatLine {
        var pts = 0; var fgm = 0; var fga = 0; var tpm = 0; var tpa = 0; var ftm = 0; var fta = 0
        var oreb = 0; var dreb = 0; var ast = 0; var stl = 0; var blk = 0; var pf = 0; var tov = 0

        fun apply(type: Int) {
            when (type) {
                RecordDataType.TWO_POINT_SHOT_MADE -> { pts += 2; fgm++; fga++ }
                RecordDataType.TWO_POINT_SHOT_MISSED -> fga++
                RecordDataType.THREE_POINT_SHOT_MADE -> { pts += 3; fgm++; fga++; tpm++; tpa++ }
                RecordDataType.THREE_POINT_SHOT_MISSED -> { fga++; tpa++ }
                RecordDataType.FREE_THROW_SHOT_MADE -> { pts += 1; ftm++; fta++ }
                RecordDataType.FREE_THROW_SHOT_MISSED -> fta++
                RecordDataType.OFFENSIVE_REBOUND -> oreb++
                RecordDataType.DEFENSIVE_REBOUND -> dreb++
                RecordDataType.ASSIST -> ast++
                RecordDataType.STEAL -> stl++
                RecordDataType.BLOCK -> blk++
                RecordDataType.FOUL, RecordDataType.DEFENSIVE_FOUL -> pf++
                RecordDataType.OFFENSIVE_FOUL -> { pf++; tov++ }
                RecordDataType.TURNOVER -> tov++
            }
        }

        fun toEntity(gameId: String, quarter: Int, player: TeamPlayerEntity) = GameDataEntity(
            gameId = gameId, quarter = quarter, playerId = player.playerId,
            playerNumber = player.playerNumber, playerName = player.playerName,
            pts = pts, fgm = fgm, fga = fga, tpm = tpm, tpa = tpa, ftm = ftm, fta = fta,
            oreb = oreb, dreb = dreb, ast = ast, stl = stl, blk = blk, pf = pf, tov = tov
        )
    }
}

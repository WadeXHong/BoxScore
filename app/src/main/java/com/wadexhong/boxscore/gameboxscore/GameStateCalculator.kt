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

    /**
     * Events that reproduce the stats of a game started by the legacy app, which only stored
     * per-quarter totals (game_data) and the opponent score. Replaying them gives back the
     * same game_data, so new events can be recorded on top. Fouls become defensive fouls and
     * turnovers plain turnovers: legacy totals cannot tell offensive fouls apart, but the
     * pf/tov columns come out the same.
     */
    fun legacyBaselineEvents(gameId: String, rows: List<GameDataEntity>, opponentScore: Int): List<GameEventEntity> {
        val quarter = rows.filter { it.hasStats() }.maxOfOrNull { it.quarter }?.coerceAtLeast(1) ?: 1
        val events = mutableListOf<GameEventEntity>()
        fun add(type: Int, count: Int, playerId: String?, q: Int) =
            repeat(count.coerceAtLeast(0)) { events += GameEventEntity(gameId = gameId, actionType = type, playerId = playerId, quarter = q) }

        (1 until quarter).forEach { add(RecordDataType.QUARTER, 1, null, it) }
        add(RecordDataType.OPPONENT_TEAM_TOTAL_SCORE, opponentScore, null, quarter)
        rows.forEach { r ->
            val twoMade = r.fgm - r.tpm
            add(RecordDataType.TWO_POINT_SHOT_MADE, twoMade, r.playerId, r.quarter)
            add(RecordDataType.TWO_POINT_SHOT_MISSED, (r.fga - r.tpa) - twoMade, r.playerId, r.quarter)
            add(RecordDataType.THREE_POINT_SHOT_MADE, r.tpm, r.playerId, r.quarter)
            add(RecordDataType.THREE_POINT_SHOT_MISSED, r.tpa - r.tpm, r.playerId, r.quarter)
            add(RecordDataType.FREE_THROW_SHOT_MADE, r.ftm, r.playerId, r.quarter)
            add(RecordDataType.FREE_THROW_SHOT_MISSED, r.fta - r.ftm, r.playerId, r.quarter)
            add(RecordDataType.OFFENSIVE_REBOUND, r.oreb, r.playerId, r.quarter)
            add(RecordDataType.DEFENSIVE_REBOUND, r.dreb, r.playerId, r.quarter)
            add(RecordDataType.ASSIST, r.ast, r.playerId, r.quarter)
            add(RecordDataType.STEAL, r.stl, r.playerId, r.quarter)
            add(RecordDataType.BLOCK, r.blk, r.playerId, r.quarter)
            add(RecordDataType.DEFENSIVE_FOUL, r.pf, r.playerId, r.quarter)
            add(RecordDataType.TURNOVER, r.tov, r.playerId, r.quarter)
        }
        return events
    }

    private fun GameDataEntity.hasStats() =
        listOf(pts, fgm, fga, tpm, tpa, ftm, fta, oreb, dreb, ast, stl, blk, pf, tov).any { it != 0 }

    /** Whole-game totals per player (quarter 0), keeping the order of [rows]. */
    fun totals(rows: List<GameDataEntity>): List<GameDataEntity> =
        rows.groupBy { it.playerId }.values.map { playerRows ->
            playerRows.reduce { acc, r ->
                acc.copy(
                    pts = acc.pts + r.pts, fgm = acc.fgm + r.fgm, fga = acc.fga + r.fga,
                    tpm = acc.tpm + r.tpm, tpa = acc.tpa + r.tpa, ftm = acc.ftm + r.ftm, fta = acc.fta + r.fta,
                    oreb = acc.oreb + r.oreb, dreb = acc.dreb + r.dreb, ast = acc.ast + r.ast,
                    stl = acc.stl + r.stl, blk = acc.blk + r.blk, pf = acc.pf + r.pf, tov = acc.tov + r.tov
                )
            }.copy(id = 0, quarter = 0)
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

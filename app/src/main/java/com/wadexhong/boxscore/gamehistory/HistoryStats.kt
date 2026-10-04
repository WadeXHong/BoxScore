package com.wadexhong.boxscore.gamehistory

import com.wadexhong.boxscore.database.entity.GameDataEntity
import com.wadexhong.boxscore.gameboxscore.GameStateCalculator

/** Aggregations behind the legacy history detail screens (HistoryTeamDataAdapter etc.). */
object HistoryStats {

    /** Column titles in legacy COLUMN_NAME_SPARSE_ARRAY order. */
    val STAT_COLUMNS = listOf("PTS", "FGM", "FGA", "TPM", "TPA", "FTM", "FTA", "OREB", "DREB", "AST", "STL", "BLK", "PF", "TOV")

    /** Quarter filter of the players table: 0 = whole game, 1..4 = that quarter. */
    const val FILTER_TOTAL = 0

    fun values(row: GameDataEntity): List<Int> = listOf(
        row.pts, row.fgm, row.fga, row.tpm, row.tpa, row.ftm, row.fta,
        row.oreb, row.dreb, row.ast, row.stl, row.blk, row.pf, row.tov
    )

    /** Team totals for quarters 1..4, the legacy chart and table always show four. */
    fun teamByQuarter(rows: List<GameDataEntity>): List<GameDataEntity> =
        (1..GameStateCalculator.STORED_QUARTERS).map { quarter -> sum(rows.filter { it.quarter == quarter }, quarter) }

    fun sum(rows: List<GameDataEntity>, quarter: Int = FILTER_TOTAL): GameDataEntity =
        rows.fold(emptyRow(quarter)) { acc, r ->
            acc.copy(
                pts = acc.pts + r.pts, fgm = acc.fgm + r.fgm, fga = acc.fga + r.fga,
                tpm = acc.tpm + r.tpm, tpa = acc.tpa + r.tpa, ftm = acc.ftm + r.ftm, fta = acc.fta + r.fta,
                oreb = acc.oreb + r.oreb, dreb = acc.dreb + r.dreb, ast = acc.ast + r.ast,
                stl = acc.stl + r.stl, blk = acc.blk + r.blk, pf = acc.pf + r.pf, tov = acc.tov + r.tov
            )
        }

    /** One row per player for [filter], ordered by jersey number. */
    fun playersFor(rows: List<GameDataEntity>, filter: Int): List<GameDataEntity> {
        val selected = if (filter == FILTER_TOTAL) GameStateCalculator.totals(rows) else rows.filter { it.quarter == filter }
        return selected.sortedWith(compareBy({ it.playerNumber.toIntOrNull() ?: Int.MAX_VALUE }, { it.playerNumber }))
    }

    fun percent(made: Int, attempted: Int): Float = if (attempted == 0) 0f else 100f * made / attempted

    /** Points from free throws, twos and threes (legacy bar chart stacks). */
    fun scoring(row: GameDataEntity): Triple<Int, Int, Int> =
        Triple(row.ftm, 2 * (row.fgm - row.tpm), 3 * row.tpm)

    /** Legacy shows a tie as a win. */
    fun isWin(yourScore: Int, opponentScore: Int) = yourScore >= opponentScore

    private fun emptyRow(quarter: Int) = GameDataEntity(
        gameId = "", quarter = quarter, playerId = "", playerNumber = "", playerName = "",
        pts = 0, fgm = 0, fga = 0, tpm = 0, tpa = 0, ftm = 0, fta = 0,
        oreb = 0, dreb = 0, ast = 0, stl = 0, blk = 0, pf = 0, tov = 0
    )
}

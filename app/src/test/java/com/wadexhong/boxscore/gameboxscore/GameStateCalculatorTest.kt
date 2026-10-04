package com.wadexhong.boxscore.gameboxscore

import com.wadexhong.boxscore.Constants.RecordDataType
import com.wadexhong.boxscore.database.entity.GameEventEntity
import com.wadexhong.boxscore.database.entity.TeamPlayerEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateCalculatorTest {

    private val starters = listOf("p1", "p2", "p3", "p4", "p5")
    private var nextId = 1L

    private fun event(type: Int, playerId: String? = null, quarter: Int = 1) =
        GameEventEntity(id = nextId++, gameId = "g", actionType = type, playerId = playerId, quarter = quarter, timestamp = nextId)

    private fun player(id: String, number: String) = TeamPlayerEntity(
        teamId = "t", playerId = id, playerName = "Name $id", playerNumber = number,
        center = 0, powerForward = 0, smallForward = 0, shootingGuard = 0, pointGuard = 0
    )

    @Test
    fun emptyLog_isFirstQuarterWithStarters() {
        val snapshot = GameStateCalculator.calculate(emptyList(), starters)
        assertEquals(GameSnapshot(onCourtIds = starters), snapshot)
    }

    @Test
    fun scores_countMadeShotsAndOpponentAdjustments() {
        val events = listOf(
            event(RecordDataType.TWO_POINT_SHOT_MADE, "p1"),
            event(RecordDataType.THREE_POINT_SHOT_MADE, "p2"),
            event(RecordDataType.FREE_THROW_SHOT_MADE, "p3"),
            event(RecordDataType.TWO_POINT_SHOT_MISSED, "p1"),
            event(RecordDataType.OPPONENT_TEAM_TOTAL_SCORE),
            event(RecordDataType.OPPONENT_TEAM_TOTAL_SCORE),
            event(RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_SCORE)
        )
        val snapshot = GameStateCalculator.calculate(events, starters)
        assertEquals(6, snapshot.yourScore)
        assertEquals(1, snapshot.opponentScore)
    }

    @Test
    fun quarter_followsQuarterEvents() {
        val events = listOf(
            event(RecordDataType.QUARTER),
            event(RecordDataType.QUARTER, quarter = 2),
            event(RecordDataType.MINUS_QUARTER, quarter = 3)
        )
        assertEquals(2, GameStateCalculator.calculate(events, starters).quarter)
    }

    @Test
    fun teamFouls_onlyCountCurrentQuarter() {
        val events = listOf(
            event(RecordDataType.DEFENSIVE_FOUL, "p1", quarter = 1),
            event(RecordDataType.OPPONENT_TEAM_FOUL, quarter = 1),
            event(RecordDataType.QUARTER, quarter = 1),
            event(RecordDataType.DEFENSIVE_FOUL, "p2", quarter = 2),
            event(RecordDataType.YOUR_TEAM_FOUL, quarter = 2),
            event(RecordDataType.OPPONENT_TEAM_FOUL, quarter = 2),
            event(RecordDataType.OPPONENT_TEAM_FOUL, quarter = 2),
            event(RecordDataType.MINUS_OPPONENT_TEAM_TOTAL_FOUL, quarter = 2)
        )
        val snapshot = GameStateCalculator.calculate(events, starters)
        assertEquals(2, snapshot.yourFouls)
        assertEquals(1, snapshot.opponentFouls)
    }

    @Test
    fun offensiveFoul_isNotATeamFoul() {
        val events = listOf(event(RecordDataType.OFFENSIVE_FOUL, "p1"))
        assertEquals(0, GameStateCalculator.calculate(events, starters).yourFouls)
    }

    @Test
    fun madeUpFoulInEarlierQuarter_doesNotChangeCurrentTeamFouls() {
        val events = listOf(
            event(RecordDataType.QUARTER, quarter = 1),
            event(RecordDataType.DEFENSIVE_FOUL, "p1", quarter = 1)
        )
        assertEquals(0, GameStateCalculator.calculate(events, starters).yourFouls)
    }

    @Test
    fun substitution_replacesPlayerInSameSlot() {
        val events = listOf(
            event(GameEventType.SUBSTITUTION_OUT, "p3"),
            event(GameEventType.SUBSTITUTION_IN, "p6")
        )
        assertEquals(listOf("p1", "p2", "p6", "p4", "p5"), GameStateCalculator.calculate(events, starters).onCourtIds)
    }

    @Test
    fun substitution_chainsAndIgnoresInvalidPairs() {
        val events = listOf(
            event(GameEventType.SUBSTITUTION_OUT, "p1"),
            event(GameEventType.SUBSTITUTION_IN, "p6"),
            // p9 is not on court: ignored
            event(GameEventType.SUBSTITUTION_OUT, "p9"),
            event(GameEventType.SUBSTITUTION_IN, "p7"),
            // p2 already on court: ignored
            event(GameEventType.SUBSTITUTION_OUT, "p6"),
            event(GameEventType.SUBSTITUTION_IN, "p2"),
            event(GameEventType.SUBSTITUTION_OUT, "p6"),
            event(GameEventType.SUBSTITUTION_IN, "p1")
        )
        assertEquals(starters, GameStateCalculator.calculate(events, starters).onCourtIds)
    }

    @Test
    fun history_containsOnlyPlayerStats() {
        val stat = event(RecordDataType.ASSIST, "p1")
        val events = listOf(
            event(RecordDataType.OPPONENT_TEAM_TOTAL_SCORE),
            event(RecordDataType.QUARTER),
            event(GameEventType.SUBSTITUTION_OUT, "p1"),
            event(GameEventType.SUBSTITUTION_IN, "p6"),
            stat
        )
        assertEquals(listOf(stat), GameStateCalculator.calculate(events, starters).history)
    }

    @Test
    fun playerStats_followLegacyColumnRules() {
        val roster = listOf(player("p1", "7"))
        val events = listOf(
            event(RecordDataType.TWO_POINT_SHOT_MADE, "p1"),
            event(RecordDataType.TWO_POINT_SHOT_MISSED, "p1"),
            event(RecordDataType.THREE_POINT_SHOT_MADE, "p1"),
            event(RecordDataType.THREE_POINT_SHOT_MISSED, "p1"),
            event(RecordDataType.FREE_THROW_SHOT_MADE, "p1"),
            event(RecordDataType.FREE_THROW_SHOT_MISSED, "p1"),
            event(RecordDataType.OFFENSIVE_REBOUND, "p1"),
            event(RecordDataType.DEFENSIVE_REBOUND, "p1"),
            event(RecordDataType.ASSIST, "p1"),
            event(RecordDataType.STEAL, "p1"),
            event(RecordDataType.BLOCK, "p1"),
            event(RecordDataType.OFFENSIVE_FOUL, "p1"),
            event(RecordDataType.DEFENSIVE_FOUL, "p1"),
            event(RecordDataType.TURNOVER, "p1")
        )
        val q1 = GameStateCalculator.playerStats("g", events, roster).single { it.quarter == 1 }

        assertEquals(6, q1.pts)
        assertEquals(2, q1.fgm)
        assertEquals(4, q1.fga)
        assertEquals(1, q1.tpm)
        assertEquals(2, q1.tpa)
        assertEquals(1, q1.ftm)
        assertEquals(2, q1.fta)
        assertEquals(1, q1.oreb)
        assertEquals(1, q1.dreb)
        assertEquals(1, q1.ast)
        assertEquals(1, q1.stl)
        assertEquals(1, q1.blk)
        assertEquals(2, q1.pf)
        assertEquals(2, q1.tov)
        assertEquals("7", q1.playerNumber)
    }

    @Test
    fun playerStats_fourRowsPerPlayerInRosterOrder() {
        val roster = listOf(player("p2", "2"), player("p1", "1"))
        val events = listOf(
            event(RecordDataType.STEAL, "p1", quarter = 3),
            event(RecordDataType.STEAL, "unknown", quarter = 1)
        )
        val rows = GameStateCalculator.playerStats("g", events, roster)

        assertEquals(8, rows.size)
        assertEquals(listOf("p2", "p2", "p2", "p2", "p1", "p1", "p1", "p1"), rows.map { it.playerId })
        assertEquals(listOf(1, 2, 3, 4), rows.take(4).map { it.quarter })
        assertEquals(1, rows.single { it.playerId == "p1" && it.quarter == 3 }.stl)
        assertTrue(rows.filterNot { it.playerId == "p1" && it.quarter == 3 }.all { it.stl == 0 })
        assertTrue(rows.all { it.gameId == "g" })
    }

    @Test
    fun totals_sumQuartersPerPlayerInOrder() {
        val roster = listOf(player("p2", "2"), player("p1", "1"))
        val events = listOf(
            event(RecordDataType.TWO_POINT_SHOT_MADE, "p1", quarter = 1),
            event(RecordDataType.THREE_POINT_SHOT_MADE, "p1", quarter = 4),
            event(RecordDataType.ASSIST, "p2", quarter = 2)
        )
        val totals = GameStateCalculator.totals(GameStateCalculator.playerStats("g", events, roster))

        assertEquals(listOf("p2", "p1"), totals.map { it.playerId })
        assertEquals(5, totals[1].pts)
        assertEquals(2, totals[1].fgm)
        assertEquals(1, totals[0].ast)
        assertTrue(totals.all { it.quarter == 0 })
    }
}

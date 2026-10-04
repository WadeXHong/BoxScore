package com.wadexhong.boxscore.teammanage

import com.wadexhong.boxscore.teammanage.teamplayers.createplayer.PlayerInputRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerInputRulesTest {

    @Test
    fun name_dropsLeadingSpacesAndRejectsTooLong() {
        assertEquals("Kobe ", PlayerInputRules.normalizeName("  Kobe "))
        assertEquals("x".repeat(20), PlayerInputRules.normalizeName("x".repeat(20)))
        assertNull(PlayerInputRules.normalizeName("x".repeat(21)))
    }

    @Test
    fun number_keepsDigitsAndDropsLeadingZero() {
        assertEquals("3", PlayerInputRules.normalizeNumber("03"))
        assertEquals("0", PlayerInputRules.normalizeNumber("0"))
        assertEquals("23", PlayerInputRules.normalizeNumber("2a3"))
        assertEquals("", PlayerInputRules.normalizeNumber("-"))
        assertNull(PlayerInputRules.normalizeNumber("123"))
    }

    @Test
    fun legality() {
        assertFalse(PlayerInputRules.isNameLegal(""))
        assertTrue(PlayerInputRules.isNameLegal("Kobe"))
        assertFalse(PlayerInputRules.isNumberLegal("", emptySet()))
        assertFalse(PlayerInputRules.isNumberLegal("8", setOf("8", "24")))
        assertTrue(PlayerInputRules.isNumberLegal("24", setOf("8")))
    }
}

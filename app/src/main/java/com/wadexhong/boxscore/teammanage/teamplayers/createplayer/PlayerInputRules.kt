package com.wadexhong.boxscore.teammanage.teamplayers.createplayer

/** Input rules of legacy CreatePlayerFragment's TextWatchers. */
object PlayerInputRules {
    const val MAX_NAME_LENGTH = 20
    const val MAX_NUMBER_LENGTH = 2

    /** New name value for [input], or null when the edit must be rejected. */
    fun normalizeName(input: String): String? =
        input.trimStart(' ').takeIf { it.length <= MAX_NAME_LENGTH }

    /** Digits only, "03" -> "3"; null when the edit must be rejected. */
    fun normalizeNumber(input: String): String? {
        var digits = input.filter(Char::isDigit)
        if (digits.length == 2 && digits[0] == '0') digits = digits.substring(1)
        return digits.takeIf { it.length <= MAX_NUMBER_LENGTH }
    }

    fun isNameLegal(name: String) = name.isNotBlank()

    fun isNumberLegal(number: String, existingNumbers: Set<String>) =
        number.isNotBlank() && number !in existingNumbers
}

package com.dumbdogdiner.sass.util

/**
 * @param i The digit for one in this place.
 * @param v The digit for five in this place.
 * @param x The digit for ten in this place.
 * @return Mappings for a digit in a certain place.
 */
private fun findNumeralSet(i: Char, v: Char, x: Char) = arrayOf(
    "",   "$i",   "$i$i",   "$i$i$i",   "$i$v", //   I  II  III  VI
    "$v", "$v$i", "$v$i$i", "$v$i$i$i", "$i$x", // V VI VII VIII IX
)

/** The ones place */
private val onesPlace = findNumeralSet('I', 'V', 'X')
/** The tens place */
private val tensPlace = findNumeralSet('X', 'L', 'C')
/** The hundreds place */
private val hunsPlace = findNumeralSet('C', 'D', 'M')

/**
 * @return The roman numeral representation of this number.
 */
fun Int.romanNumeral() = when {
    this > 0 -> this.romanNumeralAssumePositive()
    this < 0 -> "-" + (-this).romanNumeralAssumePositive()
    else -> "$this" // if zero, just use "0"
}

/**
 * @return The roman numeral representation of this number, assuming it is positive.
 */
private fun Int.romanNumeralAssumePositive() =
    "M".repeat(this / 1000) +
            hunsPlace[(this / 100) % 10] +
            tensPlace[(this / 10) % 10] +
            onesPlace[this % 10]
package com.dumbdogdiner.sass.util

/**
 * Take in digits [i], [v], and [x], and create mappings for a digit in that place.
 */
private fun findNumeralSet(i: Char, v: Char, x: Char) = arrayOf(
    "", "$i", "$i$i", "$i$i$i", "$i$v", //           I  II  III  VI
    "$v", "$v$i", "$v$i$i", "$v$i$i$i", "$i$x", // V VI VII VIII IX
)

/** The ones place */
private val onesPlace = findNumeralSet('I', 'V', 'X')
/** The tens place */
private val tensPlace = findNumeralSet('X', 'L', 'C')
/** The hundreds place */
private val hunsPlace = findNumeralSet('C', 'D', 'M')

/**
 * Returns the roman numeral representation of this number.
 */
fun Int.romanNumeral() = when {
    this > 0 -> this.romanNumeralAssumePositive()
    this < 0 -> "-" + (-this).romanNumeralAssumePositive()
    else -> "$this" // if zero, just use "0"
}

/**
 * Returns the roman numeral representation of this number, assuming it is positive.
 */
private fun Int.romanNumeralAssumePositive() =
    "M".repeat(this / 1000) +
        hunsPlace[(this / 100) % 10] +
        tensPlace[(this / 10) % 10] +
        onesPlace[this % 10]

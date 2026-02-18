package com.harrrshith.moowe.utils.extensions

import kotlin.math.pow
import kotlin.math.round

fun Double.format(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    return (round(this * factor) / factor).toString()
}

fun Int.withCommas(): String = this.toString().addCommas()

private fun String.addCommas(): String {
    val parts = this.split(".")
    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1)

    val reversed = integerPart.reversed()
    val grouped = reversed.chunked(3).joinToString(",")
    val formattedInt = grouped.reversed()

    return if (decimalPart != null) "$formattedInt.$decimalPart" else formattedInt
}
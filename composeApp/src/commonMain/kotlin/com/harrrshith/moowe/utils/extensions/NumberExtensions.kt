package com.harrrshith.moowe.utils.extensions

import kotlin.math.pow
import kotlin.math.round

fun Double.format(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    return (round(this * factor) / factor).toString()
}
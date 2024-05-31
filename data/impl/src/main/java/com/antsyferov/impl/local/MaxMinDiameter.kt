package com.antsyferov.impl.local

import kotlin.math.round

data class MaxMinDiameter(
    val max: Double,
    val min: Double
) {
    fun toRange() = min.round(2).toFloat().rangeTo(max.round(2).toFloat())

}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

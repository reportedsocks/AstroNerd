package com.antsyferov.astronerd.ui

import java.math.RoundingMode

object UnitUtils {
    fun roundDouble(input: Double, digits: Int = 3): Double {
        return input.toBigDecimal().setScale(digits, RoundingMode.HALF_UP).toDouble()
    }
}
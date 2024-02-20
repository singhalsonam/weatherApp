package com.demo.weatherapp.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object Util {
    fun formatTemp(number: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number)
    }
}
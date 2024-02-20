package com.demo.weatherapp.data.model

import androidx.annotation.Keep
import com.demo.weatherapp.utils.Constants
import java.time.LocalDate

@Keep
data class WeatherForecast(
    var date:LocalDate? = null,
    var temp: String?    = null,
    var description : String? = null,
    var icon: String? = null,
    var city: String = Constants.UNKNOWN
)

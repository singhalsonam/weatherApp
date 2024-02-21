package com.demo.weatherapp.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.weatherapp.data.model.Forecast
import com.demo.weatherapp.data.model.WeatherForecast
import com.demo.weatherapp.utils.Constants
import com.demo.weatherapp.utils.OneTimeEvent
import com.demo.weatherapp.utils.Util
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ForecastViewModel : ViewModel() {
    var forecastRepo: ForecastRepository = ForecastRepository()
    private var cityLiveData = MutableLiveData<String>()
    private val _isError = MutableLiveData<OneTimeEvent<Boolean>>()
    val isError: LiveData<OneTimeEvent<Boolean>> = _isError
    private var weeklyWeatherForecastLiveData = MutableLiveData<List<WeatherForecast>>()

    /**
     * Method to call weather forecast api
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherForecast(zipCode: String) {
        viewModelScope.launch {
            prepareData(forecastRepo.getWeatherForecast(zipCode))
        }
    }

    /**
     * Method to prepare weather app data from API response
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareData(forecastResponse: Response<Forecast?>) {
        if (forecastResponse.isSuccessful) {
            forecastResponse.body()?.let {
                _isError.value = OneTimeEvent(false)
                cityLiveData.postValue(it.city?.name ?: Constants.UNKNOWN)
                weeklyWeatherForecastLiveData.postValue(getWeeklyWeatherForecast(it.list))
            } ?: run {
                _isError.value = OneTimeEvent(true)
            }
        } else {
            Log.d("ForecastRepo", "onFailure")
            _isError.value = OneTimeEvent(true)
        }
    }

    /**
     * Method to observe forecast live data
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun observeForecastLiveData() = weeklyWeatherForecastLiveData

    /**
     * Method to observe city live data
     */
    fun observeCityLiveData() = cityLiveData

    /**
     * Method to calculate weekly weather forecast
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getWeeklyWeatherForecast(list: List<com.demo.weatherapp.data.model.List>): List<WeatherForecast> {
        val weatherForecastList = arrayListOf<WeatherForecast>()
        val weatherMap = mutableMapOf<LocalDate, Pair<Double, MutableMap<String, Int>>>()

        for (i in list.indices) {

            val weatherObj = list[i]
            val dateTimeString = weatherObj.dtTxt
            val temperature = weatherObj.main?.temp
            val description =
                weatherObj.weather[0].description
            val localDate =
                LocalDate.parse(dateTimeString!!.split(" ")[0], DateTimeFormatter.ISO_LOCAL_DATE)

            weatherMap.compute(localDate) { _, value ->
                if (value == null) {
                    temperature!! to mutableMapOf(description!! to 1)
                } else {
                    val (totalTemp, descriptions) = value
                    descriptions[description!!] = descriptions.getOrDefault(description, 0) + 1
                    totalTemp + temperature!! to descriptions
                }
            }
        }

        val weatherIconMap = mapOf(
            "clear sky" to "01d",
            "few clouds" to "02d",
            "scattered clouds" to "03d",
            "broken clouds" to "04d",
            "shower rain" to "09d",
            "rain" to "10d",
            "thunderstorm" to "11d",
            "snow" to "13d",
            "mist" to "50d"
        )

        for ((date, data) in weatherMap) {
            val (totalTemp, descriptions) = data
            val avgTemp = Util.formatTemp(totalTemp / list.size)
            val mostFrequentDescription = descriptions.maxByOrNull { it.value }!!.key
            val icon = getIcon(weatherIconMap.getOrElse(mostFrequentDescription) { "unknown.png" })
            weatherForecastList.add(
                WeatherForecast(
                    date,
                    ("$avgTemp°C"),
                    mostFrequentDescription,
                    icon
                )
            )
        }
        return weatherForecastList
    }

    /**
     * Method to create icon
     *
     * @param icon is the weather icon from json
     */
    private fun getIcon(icon: String): String {
        return "https://openweathermap.org/img/wn/${icon}@2x.png"
    }
}
package com.demo.weatherapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.weatherapp.utils.NetworkUtils
import com.demo.weatherapp.utils.OneTimeEvent
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    var forecastRepo: ForecastRepository = ForecastRepository()
    val isError: LiveData<OneTimeEvent<Boolean>> = forecastRepo.isError

    /**
     * Method to call weather forecast api
     */
    fun getWeatherForecast(zipCode: String) {
        if (NetworkUtils().isOnline()) {
            viewModelScope.launch {
                forecastRepo.getWeatherForecast(zipCode)
            }
        }
    }

    /**
     * Method to observe forecast live data
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun observeForecastLiveData() = forecastRepo.observeForecastLiveData()

    /**
     * Method to observe city live data
     */
    fun observeCityLiveData(): LiveData<String> {
        return forecastRepo.observeCityLiveData()
    }

}
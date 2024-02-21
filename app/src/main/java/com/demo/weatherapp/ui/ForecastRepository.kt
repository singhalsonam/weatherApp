package com.demo.weatherapp.ui

import com.demo.weatherapp.data.model.Forecast
import com.demo.weatherapp.data.source.remote.ForcastApi
import com.demo.weatherapp.data.source.remote.RetrofitInstance
import com.demo.weatherapp.utils.Constants
import retrofit2.Response

class ForecastRepository {
    /**
     * Method to get weather forecast from api
     */
    suspend fun getWeatherForecast(zipCode: String) : Response<Forecast?> {
        return RetrofitInstance.getInstance().create(ForcastApi::class.java).getWeatherData(
            "$zipCode,${Constants.COUNTRY_CODE}",
            Constants.APP_ID,
            Constants.METRIC
        )

    }
}
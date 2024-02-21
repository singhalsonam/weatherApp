package com.demo.weatherapp.data.source.remote

import com.demo.weatherapp.data.model.Forecast
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ForcastApi {
    @GET("forecast?")
    suspend fun getWeatherData(
        @Query("zip") zipCode: String,
        @Query("APPID") appId: String,
        @Query("units") units: String,
    ): Response<Forecast?>
}
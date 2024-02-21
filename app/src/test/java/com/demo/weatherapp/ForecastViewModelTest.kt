package com.demo.weatherapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.demo.weatherapp.ui.ForecastRepository
import com.demo.weatherapp.ui.ForecastViewModel
import com.demo.weatherapp.utils.NetworkUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Test case [ForecastViewModelTest]
 */

@ExperimentalCoroutinesApi
class ForecastViewModelTest {
    private lateinit var viewModel: ForecastViewModel
    private lateinit var forecastRepo: ForecastRepository
    private lateinit var networkUtils: NetworkUtils

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        forecastRepo = mockk()
        networkUtils = mockk()
        viewModel = ForecastViewModel()
        viewModel.forecastRepo = forecastRepo
    }

    @Test
    fun `test getWeatherForecast`() {
        val zipCode = "12345"
        // Mock NetworkUtils response
        coEvery { networkUtils.isOnline(Application()) } returns true
        // Mock forecastRepo method call
        viewModel.getWeatherForecast(zipCode)
        // verify if the method was called with correct parameter
        coVerify { forecastRepo.getWeatherForecast(zipCode) }
    }

    @Test
    fun `test observeCityLiveData`() {
        // Create a mock LiveData
        val mockCityLiveData = MutableLiveData<String>()
        // Mock forecastRepo observeCityLiveData() method
        every { forecastRepo.observeCityLiveData() } returns mockCityLiveData
        // Create an observer
        val observer = mockk<Observer<String>>(relaxed = true)
        // Observe the LiveData
        viewModel.observeCityLiveData().observeForever(observer)
        // Set value to LiveData
        val cityName = "New York"
        mockCityLiveData.value = cityName
        // Verify that the observer was called with the correct value
        verify { observer.onChanged(cityName) }
    }
}
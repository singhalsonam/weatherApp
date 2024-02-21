package com.demo.weatherapp

import com.demo.weatherapp.data.model.City
import com.demo.weatherapp.data.model.Forecast
import com.demo.weatherapp.data.model.Main
import com.demo.weatherapp.data.model.Weather
import com.demo.weatherapp.ui.ForecastRepository
import com.demo.weatherapp.ui.ForecastViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Test case [ForecastViewModelTest]
 */

@ExperimentalCoroutinesApi
class ForecastViewModelTest {
    // This dispatcher controls the execution of coroutines in tests
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    // The ViewModel to be tested
    private lateinit var viewModel: ForecastViewModel

    // Mocked dependencies
    private val mockRepository: ForecastRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastViewModel()
        viewModel.forecastRepo = mockRepository
    }

    @After
    fun tearDown() {
        // Cleanup
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful API response`() {
        // Mocking a successful API response
        val response = Forecast(
            city = City(name = "CityName"),
            list = arrayListOf(com.demo.weatherapp.data.model.List(
                    dtTxt = "2024-02-21 12:00:00",
                    main = Main(temp = 20.0),
                    weather = arrayListOf(Weather(description = "Sunny"))
                )
            )
        )
        // Mocking a successful API response
        coEvery { mockRepository.getWeatherForecast(any()) } returns retrofit2.Response.success(response)

        // Calling the ViewModel method
        viewModel.getWeatherForecast("121003")

        // Ensuring that the cityLiveData is updated with correct city name
        assertEquals("CityName", viewModel.observeCityLiveData().value)

        // Ensuring that isError LiveData is not set to true
        assertNotNull(viewModel.isError.value)
        assertEquals(false.toString(), viewModel.isError.value.toString())
    }

    @Test
    fun `test null API response`() {
        // Mocking a null API response
        coEvery { mockRepository.getWeatherForecast(any()) } returns Response.success(null)

        // Calling the ViewModel method
        viewModel.getWeatherForecast("12345")

        // Ensuring that isError LiveData is set to true
        assertNotNull(viewModel.isError.value)
        assertEquals(true.toString(), viewModel.isError.value.toString())
    }
}
package com.demo.weatherapp.ui

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.demo.weatherapp.data.model.WeatherForecast
import com.demo.weatherapp.databinding.ActivityMainBinding
import com.demo.weatherapp.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var forecastAdapter: ForecastAdapter
    private val forecastViewModel: ForecastViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etZipcode.setOnEditorActionListener { textView, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (NetworkUtils().isOnline(this)) forecastViewModel.getWeatherForecast(textView.text.toString())
                else updateView(true)
            }
            return@setOnEditorActionListener false
        }
        observeForecast()
    }

    /**
     * To observe livedata from ForecastViewModel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeForecast() {
        forecastViewModel.isError.observe(this, Observer { oneTimeEvent ->
            oneTimeEvent.consume {
                updateView(it)
            }
        })
        forecastViewModel.observeForecastLiveData().observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.layoutCurrentWeather.tvCurrentTemp.text = it[0].temp
                Glide.with(this)
                    .load(it[0].icon)
                    .into(binding.layoutCurrentWeather.ivIcon)
                prepareForecastList(it)
            }
        })
        forecastViewModel.observeCityLiveData().observe(this, Observer {
            binding.tvCity.text = it
        })
    }

    /**
     * Method to prepare weekly weather forecast list
     */
    private fun prepareForecastList(weeklyWeatherForecastLiveData: List<WeatherForecast>) {
        forecastAdapter = ForecastAdapter()
        binding.rvWeather.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = forecastAdapter
        }
        forecastAdapter.setForecastList(weeklyWeatherForecastLiveData)
    }

    private fun updateView(isError : Boolean) {
        val visibility = if(isError) View.GONE else View.VISIBLE
        if(isError) {
            Toast.makeText(
                this,
                "Something went wrong, please try again later!",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.apply {
            tvCity.visibility = visibility
            layoutCurrentWeather.root.visibility = visibility
            tvWeekly.visibility = visibility
            rvWeather.visibility = visibility
        }
    }
}

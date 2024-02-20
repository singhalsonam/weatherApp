package com.demo.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.weatherapp.data.model.Forecast
import com.demo.weatherapp.data.model.Weather
import com.demo.weatherapp.data.model.WeatherForecast
import com.demo.weatherapp.databinding.LayoutCurrentWeatherBinding

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    private var weeklyWeatherForecastLiveData = emptyList<WeatherForecast>()
    fun setForecastList(weeklyWeatherForecastLiveData: List<WeatherForecast>) {
        this.weeklyWeatherForecastLiveData = weeklyWeatherForecastLiveData
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LayoutCurrentWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutCurrentWeatherBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun getItemCount(): Int {
        return weeklyWeatherForecastLiveData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherForecast = weeklyWeatherForecastLiveData[position]
        Glide.with(holder.itemView)
            .load(weatherForecast.icon)
            .into(holder.binding.ivIcon)
        holder.binding.tvCurrentTemp.text = weatherForecast.temp
        holder.binding.tvDesc.text = weatherForecast.description
        holder.binding.tvDate.text = weatherForecast.date.toString()
    }
}
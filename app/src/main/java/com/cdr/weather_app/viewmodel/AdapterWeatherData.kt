package com.cdr.weather_app.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.ItemWeatherDataBinding
import com.cdr.weather_app.model.WeatherData

class AdapterWeatherData(private val weatherData: List<WeatherData>) : BaseAdapter(),
    View.OnClickListener {
    override fun getCount(): Int = weatherData.size

    override fun getItem(p0: Int): WeatherData = weatherData[p0]

    override fun getItemId(p0: Int): Long = weatherData[p0].id!!.toLong()

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        val binding = p1?.tag as ItemWeatherDataBinding? ?: createBinding(p2.context)

        val data = weatherData[p0]
        val temperature = String.format("%.2f", data.temperature?.minus(273.15)) + " °C"

        with(binding) {
            cityTextView.text = data.name
            descriptionTextView.text = "\"${data.description}\""
            windSpeedTextView.text = "Wind Speed: ${data.windSpeed} m/c"
            temperatureTextView.text = "Temperature: $temperature"
            descriptionImageView.setImageResource(createDescriptionImageView(data.description))
        }

        return binding.root
    }

    override fun onClick(p0: View?) {
        TODO("Add weather data in favorites")
    }

    private fun createBinding(context: Context): ItemWeatherDataBinding {
        val binding = ItemWeatherDataBinding.inflate(LayoutInflater.from(context))
        binding.isFavoriteButton.setOnClickListener(this)
        binding.root.tag = binding

        return binding
    }

    private fun createDescriptionImageView(description: String?) = when (description) {
        "Clear" -> R.drawable.ic_clear
        "Clouds" -> R.drawable.ic_cloud
        "Snow" -> R.drawable.ic_snow
        "Rain" -> R.drawable.ic_rain
        "Mist" -> R.drawable.ic_mist
        else -> R.drawable.ic_error
    }
}

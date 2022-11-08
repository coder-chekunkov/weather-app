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

typealias OnDeletePressedListener = (WeatherData) -> Unit

class AdapterFavoriteData(
    private val favoriteData: ArrayList<WeatherData>,
    private val onDeletePressedListener: OnDeletePressedListener
) : BaseAdapter(), View.OnClickListener {
    override fun getCount(): Int = favoriteData.size

    override fun getItem(p0: Int): WeatherData = favoriteData[p0]

    override fun getItemId(p0: Int): Long = favoriteData[p0].id!!.toLong()

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        val binding = p1?.tag as ItemWeatherDataBinding? ?: createBinding(p2.context)

        val data = favoriteData[p0]

        with(binding) {
            cityTextView.text = data.name
            descriptionTextView.text = "\"${data.description}\""
            temperatureTextView.text =
                "Temperature: ${String.format("%.2f", data.temperature?.minus(273.15)) + " °C"}"
            windSpeedTextView.text = "Wind Speed: ${data.windSpeed} m/c"
            descriptionImageView.setImageResource(createDescriptionImageView(data.description))
            itemButton.setImageResource(R.drawable.ic_delete)
            itemButton.tag = data
        }

        return binding.root
    }

    override fun onClick(p0: View) {
        val data = p0.tag as WeatherData
        onDeletePressedListener.invoke(data)
    }

    private fun createBinding(context: Context): ItemWeatherDataBinding {
        val binding = ItemWeatherDataBinding.inflate(LayoutInflater.from(context))

        binding.itemButton.setOnClickListener(this)
        binding.root.tag = binding
        return binding
    }

    private fun createDescriptionImageView(description: String?): Int = when (description) {
        "Clear" -> R.drawable.ic_clear
        "Clouds" -> R.drawable.ic_cloud
        "Snow" -> R.drawable.ic_snow
        "Rain" -> R.drawable.ic_rain
        "Mist" -> R.drawable.ic_mist
        else -> R.drawable.ic_error
    }
}
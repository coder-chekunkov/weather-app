package com.cdr.weather_app.screens.all_cities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.ItemAllCitiesBinding
import com.cdr.weather_app.model.all_cities_worker.AllCities

interface OnCityActionListener {
    fun onCityLike(city: AllCities)
    fun onCityInfo(city: AllCities)
}

class AllCitiesAdapter(
    private val onCityActionListener: OnCityActionListener
) :
    RecyclerView.Adapter<AllCitiesAdapter.AllCitiesViewHolder>(),
    View.OnClickListener {

    var favoritesCities: List<Long> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    var data: List<AllCities> = emptyList()

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllCitiesBinding.inflate(inflater, parent, false)

        binding.itemLikeCity.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return AllCitiesViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AllCitiesViewHolder, position: Int) {
        val city = data[position]
        val context = holder.itemView.context

        with(holder.binding) {
            itemLikeCity.tag = city
            root.tag = city

            val isLiked = favoritesCities.indexOfFirst { it == city.id }
            val color = if (isLiked != -1) R.color.red else R.color.darkGreyDayTheme

            cityTextView.text = city.name
            descriptionTextView.text = "\"${city.description}\""
            windSpeedTextView.text = "Wind Speed: ${city.windSpeed} m/c"
            temperatureTextView.text = "Temperature: ${createTemperatureString(city)}"
            descriptionImageView.setImageResource(createDescriptionImageView(city.description))
            itemLikeCity.setColorFilter(
                ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    override fun onClick(view: View) {
        val city: AllCities = view.tag as AllCities

        when (view.id) {
            R.id.itemLikeCity -> onCityActionListener.onCityLike(city)
            else -> onCityActionListener.onCityInfo(city)
        }
    }

    private fun createTemperatureString(city: AllCities) =
        String.format("%.2f", city.temperature?.minus(273.15)) + " °C"

    private fun createDescriptionImageView(description: String?) = when (description) {
        "Clear" -> R.drawable.ic_clear
        "Clouds" -> R.drawable.ic_cloud
        "Snow" -> R.drawable.ic_snow
        "Rain" -> R.drawable.ic_rain
        "Mist" -> R.drawable.ic_mist
        else -> R.drawable.ic_error
    }

    class AllCitiesViewHolder(val binding: ItemAllCitiesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
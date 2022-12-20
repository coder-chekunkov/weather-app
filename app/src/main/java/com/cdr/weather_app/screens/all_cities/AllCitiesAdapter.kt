package com.cdr.weather_app.screens.all_cities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.ItemAllCitiesBinding
import com.cdr.weather_app.model.all_cities_worker.AllCities

class AllCitiesDiffUtil(
    private val oldList: List<AllCities>, private val newList: List<AllCities>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCity = oldList[oldItemPosition]
        val newCity = newList[newItemPosition]

        return oldCity.id == newCity.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCity = oldList[oldItemPosition]
        val newCity = newList[newItemPosition]

        return oldCity == newCity
    }
}

class AllCitiesAdapter : RecyclerView.Adapter<AllCitiesAdapter.AllCitiesViewHolder>() {

    var data: List<AllCities> = emptyList()
        set(newValue) {
            val diffUtil = AllCitiesDiffUtil(field, newValue)
            field = newValue
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            diffUtilResult.dispatchUpdatesTo(this@AllCitiesAdapter)
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllCitiesBinding.inflate(inflater, parent, false)

        return AllCitiesViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AllCitiesViewHolder, position: Int) {
        val city = data[position]


        with(holder.binding) {
            cityTextView.text = city.name
            descriptionTextView.text = "\"${city.description}\""
            windSpeedTextView.text = "Wind Speed: ${city.windSpeed} m/c"
            temperatureTextView.text = "Temperature: ${createTemperatureString(city)}"
            descriptionImageView.setImageResource(createDescriptionImageView(city.description))
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
package com.cdr.weather_app.screens.favorites_cities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.ItemFavoriteCitiesBinding
import com.cdr.weather_app.model.favorite_cities_worker.FavoriteCities

class FavoriteCitiesDiffUtil(
    private val oldList: List<FavoriteCities>,
    private val newList: List<FavoriteCities>
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

interface FavoriteCitiesActionListener {
    fun onFavoriteCityInfo(city: FavoriteCities)
    fun onFavoriteCityRemove(city: FavoriteCities)
}

class FavoriteCitiesAdapter(private val favoriteCitiesActionListener: FavoriteCitiesActionListener) :
    RecyclerView.Adapter<FavoriteCitiesAdapter.FavoriteCitiesViewHolder>(), View.OnClickListener {

    var data: List<FavoriteCities> = emptyList()
        set(newValue) {
            val diffUtil = FavoriteCitiesDiffUtil(field, newValue)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
            field = newValue
            diffUtilResult.dispatchUpdatesTo(this@FavoriteCitiesAdapter)
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFavoriteCitiesBinding.inflate(inflater, parent, false)

        binding.itemMore.setOnClickListener(this)

        return FavoriteCitiesViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteCitiesViewHolder, position: Int) {
        val city = data[position]

        with(holder.binding) {
            cityTextView.text = city.name
            descriptionTextView.text = city.description
            temperatureTextView.text = "Temperature: ${createTemperatureString(city)}"
            descriptionImageView.setImageResource(createDescriptionImageView(city.description))
            itemMore.tag = city
        }
    }

    private fun createTemperatureString(city: FavoriteCities) =
        String.format("%.2f", city.temperature?.minus(273.15)) + " °C"

    private fun createDescriptionImageView(description: String?) = when (description) {
        "Clear" -> R.drawable.ic_clear
        "Clouds" -> R.drawable.ic_cloud
        "Snow" -> R.drawable.ic_snow
        "Rain" -> R.drawable.ic_rain
        "Mist" -> R.drawable.ic_mist
        else -> R.drawable.ic_error
    }

    override fun onClick(view: View) {
        if (view.id == R.id.itemMore) showPopupMenu(view)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val city = view.tag as FavoriteCities

        popupMenu.menu.add(0, ID_INFO, Menu.NONE, "Info")
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_INFO -> favoriteCitiesActionListener.onFavoriteCityInfo(city)
                ID_REMOVE -> favoriteCitiesActionListener.onFavoriteCityRemove(city)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    class FavoriteCitiesViewHolder(val binding: ItemFavoriteCitiesBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val ID_INFO = 1
        private const val ID_REMOVE = 2
    }
}
package com.cdr.weather_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cdr.weather_app.databinding.ActivityFavoritesBinding
import com.cdr.weather_app.model.StorageWorker.FavoriteDataStorageWorker
import com.cdr.weather_app.model.WeatherData
import com.cdr.weather_app.viewmodel.AdapterFavoriteData

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: AdapterFavoriteData
    private lateinit var favoriteData: ArrayList<WeatherData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.backButton.setOnClickListener { clickButtonBack() }
        favoriteData = savedInstanceState?.getParcelableArrayList(TAG_FAVORITE_DATA)
            ?: FavoriteDataStorageWorker().readFavoriteData(this)
        renderUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TAG_FAVORITE_DATA, favoriteData)
    }

    private fun clickButtonBack() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun renderUI() {
        adapter = AdapterFavoriteData(favoriteData) { deleteFavoriteData(it) }
        binding.listView.adapter = adapter
    }

    private fun deleteFavoriteData(weatherData: WeatherData) {
        favoriteData.remove(weatherData)
        FavoriteDataStorageWorker().saveFavoriteData(this, favoriteData)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        val TAG_FAVORITE_DATA = "TAG_FAVORITE_DATA"
    }
}
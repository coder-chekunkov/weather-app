package com.cdr.weather_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cdr.weather_app.databinding.ActivityMainBinding
import com.cdr.weather_app.model.GetWeatherData
import com.cdr.weather_app.model.StorageWorker.FavoriteDataStorageWorker
import com.cdr.weather_app.model.StorageWorker.WeatherDataStorageWorker
import com.cdr.weather_app.model.WeatherData
import com.cdr.weather_app.viewmodel.AdapterWeatherData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var weatherData = ArrayList<WeatherData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.favoritesButton.setOnClickListener { clickButtonFavorites() }
        binding.refreshButton.setOnClickListener { clickButtonRefresh() }

        if (isConnection()) getWeatherData() else {
            binding.refreshButton.isClickable = false
            binding.favoritesButton.isClickable = false
        }
    }

    private fun clickButtonFavorites() {
        val intent = Intent(this, FavoritesActivity::class.java)
        startActivity(intent)
    }

    private fun clickButtonRefresh() = GetWeatherData().downloadWeatherData(this, binding)

    fun renderUI(context: Context, binding: ActivityMainBinding) {
        weatherData = ArrayList(WeatherDataStorageWorker().readWeatherData(context))

        val favoriteData = FavoriteDataStorageWorker().readFavoriteData(context)

        binding.listView.adapter = AdapterWeatherData(weatherData, favoriteData)
    }

    private fun getWeatherData() {
        GetWeatherData().downloadWeatherData(this, binding)
        binding.internetConnectionImageVIew.visibility = View.GONE
        binding.internetConnectionTextView.visibility = View.GONE
    }

    @SuppressLint("NewApi")
    private fun isConnection(): Boolean {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}
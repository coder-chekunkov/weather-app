package com.cdr.weather_app.model.favorite_cities_worker

import android.util.Log
import com.cdr.core.model.Repository
import com.cdr.weather_app.model.API
import com.cdr.weather_app.model.WeatherResponse
import com.cdr.weather_app.model.storage_worker.StorageFavoriteCity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

typealias FavoriteCitiesListener = (favoriteCities: List<FavoriteCities>) -> Unit

class FavoriteCitiesRepository : Repository {

    private var cities = mutableListOf<FavoriteCities>()
    private val favoriteCitiesListeners = mutableListOf<FavoriteCitiesListener>()

    private val retrofitClient: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val weatherService = retrofitClient.create(API::class.java)

    fun downloadData(favoriteCitiesStorage: List<StorageFavoriteCity>) {
        cities.clear()
        for (city in favoriteCitiesStorage) {
            val call = weatherService.getWeatherData("${city.linkName},ru", APP_ID)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>, response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d(LOG_TAG_SUCCESSFUL, "Data has been downloaded - favorite.")
                        val weatherResponse = response.body()
                        cities.add(
                            FavoriteCities(
                                id = weatherResponse?.id?.toLong(),
                                name = weatherResponse?.name,
                                description = weatherResponse?.weather?.get(0)?.main,
                                temperature = weatherResponse?.main?.temp,
                                linkName = city.linkName
                            )
                        )
                        notifyChanges()
                    } else Log.d(LOG_TAG_ERROR, "Code: ${response.code()}")
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.d(LOG_TAG_ERROR, t.toString())
                }
            })
        }
    }

    fun removeFavoriteCity(city: FavoriteCities) {
        val index = cities.indexOfFirst { it.id == city.id }
        if (index == -1) return

        cities = ArrayList(cities)
        cities.removeAt(index)

        notifyChanges()
    }

    fun addListener(favoriteCitiesListener: FavoriteCitiesListener) {
        favoriteCitiesListeners.add(favoriteCitiesListener)
        favoriteCitiesListener.invoke(cities)
    }

    fun removeListener(favoriteCitiesListener: FavoriteCitiesListener) {
        favoriteCitiesListeners.remove(favoriteCitiesListener)
        favoriteCitiesListener.invoke(cities)
    }

    private fun notifyChanges() = favoriteCitiesListeners.forEach { it.invoke(cities) }

    companion object {
        private const val LOG_TAG_ERROR = "WEATHER_ERROR"
        private const val LOG_TAG_SUCCESSFUL = "WEATHER_SUCCESSFUL"

        private const val BASE_URL = "http://api.openweathermap.org/"
        private const val APP_ID = "7b79fc58e45785b7be4c3e6688a9a8b3"
    }
}
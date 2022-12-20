package com.cdr.weather_app.model.all_cities_worker

import android.util.Log
import com.cdr.core.model.Repository
import com.cdr.weather_app.model.API
import com.cdr.weather_app.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

typealias AllCitiesListener = (allCities: List<AllCities>) -> Unit

class AllCitiesRepository : Repository {

    private val cities = mutableListOf<AllCities>()
    private val allCitiesListeners = mutableListOf<AllCitiesListener>()

    private val retrofitClient: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val weatherService = retrofitClient.create(API::class.java)

    fun downloadData() {
        for (city in NAME_OF_CITIES) {
            val call = weatherService.getWeatherData("$city,ru", APP_ID)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>, response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d(LOG_TAG_SUCCESSFUL, "Data has been downloaded.")
                        val weatherResponse = response.body()
                        cities.add(
                            AllCities(
                                id = weatherResponse?.id?.toLong(),
                                name = weatherResponse?.name,
                                description = weatherResponse?.weather?.get(0)?.main,
                                temperature = weatherResponse?.main?.temp,
                                windSpeed = weatherResponse?.wind?.speedWind,
                                linkName = city
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

    fun addListener(listener: AllCitiesListener) {
        allCitiesListeners.add(listener)
        listener.invoke(cities)
    }

    fun removeListener(listener: AllCitiesListener) {
        allCitiesListeners.remove(listener)
        listener.invoke(cities)
    }

    fun clearData() {
        cities.clear()
        notifyChanges()
    }

    private fun notifyChanges() = allCitiesListeners.forEach { it.invoke(cities) }

    companion object {
        private const val LOG_TAG_ERROR = "WEATHER_ERROR"
        private const val LOG_TAG_SUCCESSFUL = "WEATHER_SUCCESSFUL"

        private const val BASE_URL = "http://api.openweathermap.org/"
        private const val APP_ID = "7b79fc58e45785b7be4c3e6688a9a8b3"
        private val NAME_OF_CITIES = arrayOf(
            "Moscow",
            "Sankt-Peterburg",
            "Volgograd",
            "Krasnodar",
            "Rostov",
            "Sochi",
            "Kaliningrad",
            "Samara",
            "Vladikavkaz",
            "Kaluga",
            "Astrakhan",
            "Vladimir",
            "Ufa",
            "Chelyabinsk",
            "Magadan",
            "Norilsk",
            "Penza",
            "Smolensk",
            "Ryazan",
            "Elista"
        )
        val ID_OF_CITIES = listOf<Long>(
            524901,
            498817,
            472757,
            542415,
            501183,
            491422,
            554234,
            499068,
            473249,
            553899,
            580497,
            473247,
            479561,
            1508291,
            2123628,
            1497337,
            511565,
            491684,
            500096,
            563514
        )
    }
}
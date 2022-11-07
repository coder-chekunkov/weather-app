package com.cdr.weather_app.model

import android.content.Context
import android.widget.Toast
import com.cdr.weather_app.MainActivity
import com.cdr.weather_app.databinding.ActivityMainBinding
import com.cdr.weather_app.model.StorageWorker.WeatherDataStorageWorker
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class GetWeatherData {

    fun downloadWeatherData(context: Context, binding: ActivityMainBinding) {
        val weatherData = ArrayList<WeatherData>()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)

        for (city in NAME_OF_CITIES) {
            val call = weatherService.getCurrentWeatherData("$city,ru", APP_ID)
            call?.enqueue(object : Callback<WeatherResponse?> {
                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        weatherData.add(
                            WeatherData(
                                id = weatherResponse?.id,
                                name = weatherResponse?.name,
                                description = weatherResponse?.weather?.get(0)?.main,
                                temperature = weatherResponse?.main?.temp,
                                windSpeed = weatherResponse?.wind?.speedWind
                            )
                        )
                    } else {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (weatherData.size == 20) {
                        WeatherDataStorageWorker().saveWeatherData(
                            context,
                            weatherData
                        )
                        Toast.makeText(context, "Data has been downloaded!", Toast.LENGTH_SHORT)
                            .show()
                        MainActivity().renderUI(context, binding)
                    }
                }

                override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                    return
                }
            })
        }
    }

    companion object {

        const val BASE_URL = "http://api.openweathermap.org/"
        const val APP_ID = "7b79fc58e45785b7be4c3e6688a9a8b3"
        val NAME_OF_CITIES = arrayOf(
            "Moscow", "Sankt-Peterburg", "Volgograd", "Krasnodar", "Rostov",
            "Sochi", "Kaliningrad", "Samara", "Vladikavkaz", "Kaluga", "Astrakhan", "Vladimir",
            "Ufa", "Chelyabinsk", "Magadan", "Norilsk", "Penza", "Smolensk", "Ryazan", "Elista"
        )
    }
}
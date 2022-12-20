package com.cdr.weather_app.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("data/2.5/weather?")
    fun getWeatherData(
        @Query("q") city: String?, @Query("APPID") app_id: String?
    ): Call<WeatherResponse>

}
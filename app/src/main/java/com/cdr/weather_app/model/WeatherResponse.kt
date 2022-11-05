package com.cdr.weather_app.model

import com.google.gson.annotations.SerializedName


class WeatherResponse {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("id")
    var id = 0

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("weather")
    var weather: ArrayList<Weather> = ArrayList()
}

class Main {
    @SerializedName("temp")
    var temp = 0f
}

class Weather {
    @SerializedName("main")
    var main: String? = null
}

class Wind {
    @SerializedName("speed")
    var speedWind = 0f
}
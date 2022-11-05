package com.cdr.weather_app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherData(
    val id: Int?,
    val name: String?,
    val description: String?,
    val temperature: Float?,
    val windSpeed: Float?
): Parcelable

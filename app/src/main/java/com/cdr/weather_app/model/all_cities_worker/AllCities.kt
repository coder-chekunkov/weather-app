package com.cdr.weather_app.model.all_cities_worker

import com.cdr.weather_app.model.all_cities_worker.AllCitiesRepository.Companion.ID_OF_CITIES

data class AllCities(
    val id: Long?,
    val name: String?,
    val description: String?,
    val temperature: Float?,
    val windSpeed: Float?,
    val linkName: String?
) {
    override fun toString(): String = "$name[Temperature: $temperature; Wind Speed: $windSpeed]"
}

fun List<AllCities>.correctSort(): List<AllCities> {
    val sortedList = mutableListOf<AllCities>()

    ID_OF_CITIES.forEach {
        for (city in this)
            if (city.id == it) sortedList.add(city)
    }

    return sortedList
}

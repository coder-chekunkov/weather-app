package com.cdr.weather_app.model.favorite_cities_worker

data class FavoriteCities(
    val id: Long?,
    val name: String?,
    val description: String?,
    val temperature: Float?,
    val linkName: String?
) {
    override fun toString(): String = "$name[Description: $description;]"
}


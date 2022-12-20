package com.cdr.weather_app

import android.app.Application
import com.cdr.core.BaseApplication
import com.cdr.weather_app.model.all_cities_worker.AllCitiesRepository

class App : Application(), BaseApplication {

    override val models: List<Any> = listOf(
        AllCitiesRepository()
    )
}
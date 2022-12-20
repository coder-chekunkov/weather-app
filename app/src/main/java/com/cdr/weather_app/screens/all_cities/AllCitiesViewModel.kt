package com.cdr.weather_app.screens.all_cities

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cdr.core.navigator.Navigator
import com.cdr.core.uiactions.UiActions
import com.cdr.core.views.BaseViewModel
import com.cdr.weather_app.R
import com.cdr.weather_app.model.all_cities_worker.AllCities
import com.cdr.weather_app.model.all_cities_worker.AllCitiesListener
import com.cdr.weather_app.model.all_cities_worker.AllCitiesRepository
import com.cdr.weather_app.model.all_cities_worker.correctSort
import com.cdr.weather_app.screens.favorites_cities.FavoriteCitiesFragment
import com.cdr.weather_app.screens.internet_connection.InternetConnectionFragment

class AllCitiesViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val allCitiesRepository: AllCitiesRepository
) : BaseViewModel() {

    private val _allCities = MutableLiveData<List<AllCities>>()
    val allCities: LiveData<List<AllCities>> = _allCities
    var isSnackbarShow = true

    private val allCitiesListener: AllCitiesListener = {
        if (it.size == 20) {
            isSnackbarShow = true
            _allCities.value = it.correctSort()
        }
    }

    init {
        allCitiesRepository.addListener(allCitiesListener)
        allCitiesRepository.downloadData()
    }

    fun launchFavoriteCitiesScreen() = navigator.launch(FavoriteCitiesFragment.Screen())

    fun launchInternetConnectionScreen() =
        navigator.launch(InternetConnectionFragment.Screen(), false)

    fun updateData() {
        _allCities.value = emptyList()
        allCitiesRepository.clearData()
        allCitiesRepository.downloadData()
    }

    fun dataHasBeenDownloadedMessage(view: View) {
        uiActions.showSnackbar(view, "Data has been downloaded!", R.color.green)
        isSnackbarShow = false
    }

    override fun onCleared() {
        super.onCleared()
        allCitiesRepository.removeListener(allCitiesListener)
    }
}
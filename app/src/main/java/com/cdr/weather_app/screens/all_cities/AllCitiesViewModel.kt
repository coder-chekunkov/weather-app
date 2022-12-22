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
import com.cdr.weather_app.model.storage_worker.StorageCitiesListener
import com.cdr.weather_app.model.storage_worker.StorageRepository
import com.cdr.weather_app.screens.favorites_cities.FavoriteCitiesFragment
import com.cdr.weather_app.screens.internet_connection.InternetConnectionFragment

class AllCitiesViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val allCitiesRepository: AllCitiesRepository,
    private val favoriteCitiesRepository: StorageRepository
) : BaseViewModel() {

    private val _allCities = MutableLiveData<List<AllCities>>()
    val allCities: LiveData<List<AllCities>> = _allCities
    var isSnackbarShow = true

    private val _favoriteIDs = MutableLiveData<List<Long>>()
    val favoriteIDs: LiveData<List<Long>> = _favoriteIDs

    private val allCitiesListener: AllCitiesListener = {
        if (it.size == 20) {
            isSnackbarShow = true
            _allCities.value = it.correctSort()
        }
    }
    private val storageCitiesListener: StorageCitiesListener = {
        val buffIDs = mutableListOf<Long>()
        it.forEach { value -> buffIDs.add(value.id) }
        _favoriteIDs.value = buffIDs
    }

    init {
        favoriteCitiesRepository.addListener(storageCitiesListener)
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

    fun likeCity(city: AllCities) = favoriteCitiesRepository.likeCity(city)
    fun moreInfo(city: AllCities) = uiActions.showToast(city.toString())

    override fun onCleared() {
        super.onCleared()
        favoriteCitiesRepository.removeListener(storageCitiesListener)
        allCitiesRepository.removeListener(allCitiesListener)
    }
}
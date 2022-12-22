package com.cdr.weather_app.screens.favorites_cities

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cdr.core.uiactions.UiActions
import com.cdr.core.views.BaseViewModel
import com.cdr.weather_app.model.favorite_cities_worker.FavoriteCities
import com.cdr.weather_app.model.favorite_cities_worker.FavoriteCitiesListener
import com.cdr.weather_app.model.favorite_cities_worker.FavoriteCitiesRepository
import com.cdr.weather_app.model.storage_worker.StorageFavoriteCity
import com.cdr.weather_app.model.storage_worker.StorageRepository

class FavoriteCitiesViewModel(
    private val uiActions: UiActions,
    private val favoriteCitiesRepository: FavoriteCitiesRepository,
    storageRepository: StorageRepository
) : BaseViewModel() {

    private var favoriteCitiesFromStorage: List<StorageFavoriteCity>

    private val _allFavoriteCities = MutableLiveData<List<FavoriteCities>?>()
    val allFavoriteCities: LiveData<List<FavoriteCities>?> = _allFavoriteCities

    private val favoriteCitiesListener: FavoriteCitiesListener = {
        _allFavoriteCities.value = it
    }

    init {
        favoriteCitiesFromStorage = storageRepository.readFavoriteCitiesFromStorage()

        favoriteCitiesRepository.addListener(favoriteCitiesListener)
        if (favoriteCitiesFromStorage.isEmpty()) _allFavoriteCities.value = null
        else favoriteCitiesRepository.downloadData(favoriteCitiesFromStorage)
    }

    fun showSnackbar(view: View, message: String, color: Int) =
        uiActions.showSnackbar(view, message, color)

    fun showInfoFavoriteCity(city: FavoriteCities) = uiActions.showToast(city.toString())
    fun removeFavoriteCity(city: FavoriteCities) = uiActions.showToast("TODO: remove -> $city")

    override fun onCleared() {
        super.onCleared()
        favoriteCitiesRepository.removeListener(favoriteCitiesListener)
    }
}
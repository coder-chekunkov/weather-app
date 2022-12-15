package com.cdr.weather_app.screens.all_cities

import com.cdr.core.navigator.Navigator
import com.cdr.core.uiactions.UiActions
import com.cdr.core.views.BaseViewModel
import com.cdr.weather_app.screens.favorites_cities.FavoriteCitiesFragment

class AllCitiesViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions
) : BaseViewModel() {


    fun launchFavoriteCitiesScreen() = navigator.launch(FavoriteCitiesFragment.Screen())
    fun updateData() = uiActions.showToast("Working!")
}
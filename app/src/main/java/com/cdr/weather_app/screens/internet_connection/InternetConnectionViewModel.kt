package com.cdr.weather_app.screens.internet_connection

import android.view.View
import com.cdr.core.navigator.Navigator
import com.cdr.core.uiactions.UiActions
import com.cdr.core.views.BaseViewModel
import com.cdr.weather_app.R
import com.cdr.weather_app.screens.all_cities.AllCitiesFragment

class InternetConnectionViewModel(
    private val navigator: Navigator, private val uiActions: UiActions
) : BaseViewModel() {

    var isFirstMessageOfError: Boolean = true

    fun showFirstMessageOfError(view: View) {
        isFirstMessageOfError = false
        showSnackbarWithErrorConnection(view)
    }

    fun showSnackbarWithErrorConnection(view: View) = uiActions.showSnackbar(
        view = view,
        message = uiActions.getString(R.string.error_message_snackbar),
        backgroundColor = R.color.red
    )

    fun launchAllCitiesScreen() = navigator.launch(AllCitiesFragment.Screen(), false)
}
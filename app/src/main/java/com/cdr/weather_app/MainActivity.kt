package com.cdr.weather_app

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.cdr.core.ActivityScopeViewModel
import com.cdr.core.navigator.IntermediateNavigator
import com.cdr.core.navigator.StackFragmentNavigator
import com.cdr.core.uiactions.AndroidUiActions
import com.cdr.core.utils.viewModelCreator
import com.cdr.core.views.FragmentHolder
import com.cdr.weather_app.databinding.ActivityMainBinding
import com.cdr.weather_app.screens.all_cities.AllCitiesFragment
import com.cdr.weather_app.screens.internet_connection.InternetConnectionFragment

class MainActivity : AppCompatActivity(), FragmentHolder {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: StackFragmentNavigator
    private val viewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            navigator = IntermediateNavigator(), uiActions = AndroidUiActions(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            toolbar = binding.toolbar,
            defaultTitle = getString(R.string.app_name),
            animations = null
        ) { if (checkInternetConnection()) AllCitiesFragment.Screen() else InternetConnectionFragment.Screen() }

        navigator.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        viewModel.navigator.setTarget(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        notifyScreenUpdates()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun notifyScreenUpdates() = navigator.notifyScreenUpdates()
    override fun getActivityScopeViewModel(): ActivityScopeViewModel = viewModel
    override fun checkInternetConnection(): Boolean = navigator.checkInternetConnection()
}
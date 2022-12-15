package com.cdr.weather_app.screens.all_cities

import android.os.Bundle
import android.view.View
import com.cdr.core.views.*
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.FragmentAllCitiesBinding

class AllCitiesFragment : BaseFragment(R.layout.fragment_all_cities), HasCustomTitle,
    HasSeveralCustomActions {

    override val viewModel by screenViewModel<AllCitiesViewModel>()
    private lateinit var binding: FragmentAllCitiesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllCitiesBinding.bind(view)
    }

    override fun getScreenTitle(): String = getString(R.string.weather_title)
    override fun getSeveralCustomActions(): SeveralCustomActions = SeveralCustomActions(
        menuRes = R.menu.actions_all_cities, onSeveralCustomActions = listOf(
            Action(
                id = R.id.launchFavoriteScreenButton,
                action = { viewModel.launchFavoriteCitiesScreen() }),
            Action(
                id = R.id.updateDataButton,
                action = { viewModel.updateData() }
            )
        )
    )

    class Screen : BaseScreen
}
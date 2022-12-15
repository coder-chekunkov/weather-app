package com.cdr.weather_app.screens.favorites_cities

import android.os.Bundle
import android.view.View
import com.cdr.core.views.*
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.FragmentFavoriteCitiesBinding

class FavoriteCitiesFragment : BaseFragment(R.layout.fragment_favorite_cities), HasCustomTitle {

    override val viewModel by screenViewModel<FavoriteCitiesViewModel>()
    private lateinit var binding: FragmentFavoriteCitiesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteCitiesBinding.bind(view)
    }


    override fun getScreenTitle(): String = getString(R.string.favorites_title)

    class Screen : BaseScreen
}
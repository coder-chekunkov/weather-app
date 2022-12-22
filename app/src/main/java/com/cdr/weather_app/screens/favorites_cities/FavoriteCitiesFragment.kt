package com.cdr.weather_app.screens.favorites_cities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdr.core.views.*
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.FragmentFavoriteCitiesBinding
import com.cdr.weather_app.model.favorite_cities_worker.FavoriteCities

class FavoriteCitiesFragment : BaseFragment(R.layout.fragment_favorite_cities), HasCustomTitle {

    override val viewModel by screenViewModel<FavoriteCitiesViewModel>()
    private lateinit var binding: FragmentFavoriteCitiesBinding
    private lateinit var adapter: FavoriteCitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteCitiesBinding.bind(view)

        adapter = FavoriteCitiesAdapter(object : FavoriteCitiesActionListener {
            override fun onFavoriteCityInfo(city: FavoriteCities) =
                viewModel.showInfoFavoriteCity(city)

            override fun onFavoriteCityRemove(city: FavoriteCities) =
                viewModel.removeFavoriteCity(city)
        })

        viewModel.allFavoriteCities.observe(viewLifecycleOwner) { favoriteCities ->

            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            if (favoriteCities == null) viewModel.showSnackbar(view, "No favorite cities!", R.color.darkBlueNightTheme)
            else {
                viewModel.showSnackbar(view, "Data has been downloaded!", R.color.green)
                renderUI(favoriteCities)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun renderUI(favoriteCities: List<FavoriteCities>) {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(requireContext().getDrawable(R.drawable.divider)!!)

        adapter.data = favoriteCities
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun getScreenTitle(): String = getString(R.string.favorites_title)

    class Screen : BaseScreen
}
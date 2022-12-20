package com.cdr.weather_app.screens.all_cities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdr.core.views.*
import com.cdr.weather_app.R
import com.cdr.weather_app.databinding.FragmentAllCitiesBinding
import com.cdr.weather_app.model.all_cities_worker.AllCities

class AllCitiesFragment : BaseFragment(R.layout.fragment_all_cities), HasCustomTitle,
    HasSeveralCustomActions {

    override val viewModel by screenViewModel<AllCitiesViewModel>()
    private lateinit var binding: FragmentAllCitiesBinding
    private lateinit var adapter: AllCitiesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllCitiesBinding.bind(view)

        adapter = AllCitiesAdapter(object : OnCityActionListener {
            override fun onCityLike(city: AllCities) = viewModel.likeCity(city)
            override fun onCityInfo(city: AllCities) = viewModel.moreInfo(city)
        })
        viewModel.favoriteIDs.observe(viewLifecycleOwner) { adapter.favoritesCities = it }

        viewModel.allCities.observe(viewLifecycleOwner) { allCitiesList ->
            if (allCitiesList.size == 20) {
                if (viewModel.isSnackbarShow) viewModel.dataHasBeenDownloadedMessage(view)

                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                renderUI(allCitiesList)
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun renderUI(allCitiesList: List<AllCities>) {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(requireContext().getDrawable(R.drawable.divider)!!)

        adapter.data = allCitiesList
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun getScreenTitle(): String = getString(R.string.weather_title)
    override fun getSeveralCustomActions(): SeveralCustomActions = SeveralCustomActions(
        menuRes = R.menu.actions_all_cities,
        onSeveralCustomActions = listOf(Action(id = R.id.launchFavoriteScreenButton, action = {
            if (checkInternetConnection()) viewModel.launchFavoriteCitiesScreen()
            else viewModel.launchInternetConnectionScreen()
        }), Action(id = R.id.updateDataButton, action = {
            if (checkInternetConnection()) viewModel.updateData()
            else viewModel.launchInternetConnectionScreen()
        }))
    )

    class Screen : BaseScreen
}
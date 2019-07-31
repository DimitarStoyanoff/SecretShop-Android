package com.stoyanoff.secretshop.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.stoyanoff.secretshop.R
import com.stoyanoff.secretshop.data.model.OpenStatus
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.presentation.common.BaseViewFragment
import kotlinx.android.synthetic.main.fragment_restaurants.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsFragment : BaseViewFragment() {

    private val viewModel : RestaurantsViewModel by viewModel()
    private val restaurantsAdapter : RestaurantsAdapter by inject()
    private var selectedSortType = SortTypes.BEST_MATCH

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.fragment_restaurants,container,false)

        return fragmentView
    }

    override fun toggleLoading(isVisible: Boolean) {
        if(isVisible) {
            progressBar.visibility = View.VISIBLE
        } else progressBar.visibility = View.GONE
    }

    override fun initUi() {
        initAdapter()
        loadData()
        initSpinners()
        initSearchView()
    }

    private fun loadData() {
        viewModel.loadData()
    }

    private fun initAdapter() {
        restaurantsAdapter.clickListener = {
            viewModel.listItemClicked(it)
        }

        with(recycler_view){
            layoutManager = LinearLayoutManager(activity)
            adapter = restaurantsAdapter
        }
    }

    private fun initSpinners() {
        sort_type_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0 -> selectedSortType = SortTypes.BEST_MATCH
                    1 -> selectedSortType = SortTypes.NEWEST
                    2 -> selectedSortType = SortTypes.RATING_AVERAGE
                    3 -> selectedSortType = SortTypes.DISTANCE
                    4 -> selectedSortType = SortTypes.POPULARITY
                    5 -> selectedSortType = SortTypes.AVERAGE_PRODUCT_PRICE
                    6 -> selectedSortType = SortTypes.DELIVERY_COSTS
                    7 -> selectedSortType = SortTypes.MIN_COST
                }
                restaurantsAdapter.setSortType(selectedSortType)
                viewModel.viewState.value?.results?.let {
                    sortRestaurants(it)
                }

            }
        }
    }

    private fun initSearchView() {
        search_view.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.viewState.value?.results?.let {
                    restaurantsAdapter.setItems(searchFilterRestaurants(it,p0!!))
                }

                return false
            }

        })
    }

    override fun initViewModelStates() {
        handleRestaurantsViewState()
    }

    private fun handleRestaurantsViewState() {
        viewModel.viewState.observe(this, Observer {
            if(it != null) {
                toggleLoading(it.showLoading)

                it.results?.let {restaurants ->
                    sortRestaurants(restaurants)
                }
            }
        })
    }

    private fun sortRestaurants(restaurants : MutableList<Restaurant> ) {
        val sorted = restaurants.sortedWith(compareBy<Restaurant> { !it.isFavorite }.thenBy { it.status }.thenBy {
            when(selectedSortType){
                SortTypes.BEST_MATCH -> {-it.sortingValues?.bestMatch!!}
                SortTypes.NEWEST -> {-it.sortingValues?.newest!!}
                SortTypes.RATING_AVERAGE -> {-it.sortingValues?.ratingAverage!!}
                SortTypes.DISTANCE ->  {it.sortingValues?.distance}
                SortTypes.POPULARITY -> {-it.sortingValues?.popularity!!}
                SortTypes.AVERAGE_PRODUCT_PRICE -> {it.sortingValues?.averageProductPrice}
                SortTypes.DELIVERY_COSTS ->  {it.sortingValues?.deliveryCosts}
                SortTypes.MIN_COST -> {it.sortingValues?.minCost}
            }
        })
        restaurantsAdapter.setItems(sorted.toMutableList())
    }

    enum class SortTypes {
        BEST_MATCH,
        NEWEST,
        RATING_AVERAGE,
        DISTANCE,
        POPULARITY,
        AVERAGE_PRODUCT_PRICE,
        DELIVERY_COSTS,
        MIN_COST
    }

    internal fun searchFilterRestaurants(fullList: MutableList<Restaurant>, query: String): MutableList<Restaurant> {
        var query = query
        query = query.toLowerCase()
        query = query.replace("\\s".toRegex(), "")

        if (query == "") return fullList

        val filteredList = ArrayList<Restaurant>()
        for (restaurant in fullList) {
            restaurant.name?.let {
                if(it.toLowerCase(Locale.ENGLISH).contains(query))
                    filteredList.add(restaurant)
            }
        }
        return filteredList.toMutableList()
    }
}
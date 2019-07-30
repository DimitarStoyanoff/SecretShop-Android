package com.stoyanoff.secretshop.presentation.restaurants

import androidx.lifecycle.MutableLiveData
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.presentation.common.BaseViewModel

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsViewModel(
    private val restaurantsViewState: RestaurantsViewState,
    private val dataSource: RestaurantsDataSource
) : BaseViewModel() {

    internal val viewState = MutableLiveData<RestaurantsViewState>().apply {
//        postValue(restaurantsViewState)
        value = restaurantsViewState
    }
    private var restaurants = mutableListOf<Restaurant>()

    internal fun loadData() {
        toggleLoadingState(true)
        restaurants = dataSource.getRestaurants()
        showResults(restaurants)
    }

    internal fun toggleLoadingState(showLoading: Boolean) {
        viewState.value?.let {
            val newState = restaurantsViewState.copy(showLoading = showLoading)
            viewState.value = newState
        }
    }

    internal fun showResults(albums: MutableList<Restaurant>) {
        viewState.value?.let {
            val newState = restaurantsViewState.copy(showLoading = false, results = albums)
            viewState.value = newState
        }
    }


    internal fun listItemClicked(item : Restaurant) {
        //TODO
    }
}
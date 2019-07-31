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
    private var favoritesList = mutableSetOf<String>()

    internal fun loadData() {
        toggleLoadingState(true)
        favoritesList = dataSource.getFavorites()
        restaurants = dataSource.getRestaurants()
        updateFavorites(this.restaurants, this.favoritesList)
        showResults(this.restaurants)
    }

    internal fun toggleLoadingState(showLoading: Boolean) {
        viewState.value?.let {
            val newState = restaurantsViewState.copy(showLoading = showLoading)
            viewState.value = newState
        }
    }

    internal fun showResults(restaurants: MutableList<Restaurant>) {
        viewState.value?.let {
            val newState = restaurantsViewState.copy(showLoading = false, results = restaurants)
            viewState.value = newState
        }
    }

    internal fun updateFavorites(restaurants : MutableList<Restaurant>, favorites: MutableSet<String>) {
        for (favorite in favorites) {
            for (restaurant in restaurants) {
                if (restaurant.name.equals(favorite))
                    restaurant.isFavorite = true
            }
        }
    }

    internal fun updateSingleFavorite(restaurants: MutableList<Restaurant>, restaurant: Restaurant) {
        for (item in restaurants) {
            if(item.name.equals(restaurant.name))
                item.isFavorite = !restaurant.isFavorite
        }
    }

    internal fun listItemClicked(item : Restaurant) {
        if(item.isFavorite) {
            dataSource.removeFromFavorites(item)
            favoritesList.remove(item.name)
        } else {
            dataSource.addToFavorites(item)
            item.name?.let {
                favoritesList.add(it)
            }
        }
        updateSingleFavorite(restaurants,item)
        showResults(restaurants)

    }
}
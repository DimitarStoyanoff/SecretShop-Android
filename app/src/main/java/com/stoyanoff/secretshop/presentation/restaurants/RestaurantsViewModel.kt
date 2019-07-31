package com.stoyanoff.secretshop.presentation.restaurants

import androidx.lifecycle.MutableLiveData
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.presentation.common.BaseViewModel
import java.util.*

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsViewModel(
    private val restaurantsViewState: RestaurantsViewState,
    private val dataSource: RestaurantsDataSource
) : BaseViewModel() {

    internal val viewState = MutableLiveData<RestaurantsViewState>().apply {
        postValue(restaurantsViewState)
    }
    internal var restaurants = mutableListOf<Restaurant>()
    private var favoritesList = mutableSetOf<String>()
    private var selectedSortType = SortTypes.BEST_MATCH

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
            val newState = restaurantsViewState.copy(showLoading = false, results = restaurants, selectedSortType = selectedSortType)
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

    //NOTE: Sorting when setting an item as favorite is not added here because of UX (looks to user like store disappears)
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



    internal fun onSearchQueryChanged(query: String) {
        showResults(searchFilterRestaurants(restaurants,query))
    }

    internal fun onSortTypeChanged(position: Int) {
        when(position){
            0 -> selectedSortType = SortTypes.BEST_MATCH
            1 -> selectedSortType = SortTypes.NEWEST
            2 -> selectedSortType = SortTypes.RATING_AVERAGE
            3 -> selectedSortType = SortTypes.DISTANCE
            4 -> selectedSortType = SortTypes.POPULARITY
            5 -> selectedSortType = SortTypes.AVERAGE_PRODUCT_PRICE
            6 -> selectedSortType = SortTypes.DELIVERY_COSTS
            7 -> selectedSortType = SortTypes.MIN_COST
        }
        sortRestaurants(restaurants)
        showResults(restaurants)
    }

    internal fun sortRestaurants(restaurants : MutableList<Restaurant> ) {
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
        this.restaurants = sorted.toMutableList()
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
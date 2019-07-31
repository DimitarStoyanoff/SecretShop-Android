package com.stoyanoff.secretshop.presentation.restaurants

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.stoyanoff.secretshop.InstantTaskExecutorRule
import com.stoyanoff.secretshop.RxImmediateSchedulerRule
import com.stoyanoff.secretshop.data.model.OpenStatus
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.data.model.SortingValues
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by L on 31/07/2019.
 * Copyright (c) 2017 Centroida. All rights reserved.
 */
@RunWith(JUnit4::class)
class RestaurantsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var dataSource: RestaurantsDataSource

    private lateinit var viewModel: RestaurantsViewModel

    val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
        3.0f,4,5.0f,5,6, 7),false)
    val restaurant2 = Restaurant("name2", OpenStatus.CLOSED, SortingValues(12.0f,2.0f,
        3.0f,4,5.0f,5,6, 7),false)

    val restaurants = mutableListOf<Restaurant>(restaurant,restaurant2)
    val favorites = mutableSetOf<String>("name")


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RestaurantsViewModel(RestaurantsViewState(), this.dataSource)
    }

    @Test
    fun loadData() {
        val spyViewModel = Mockito.spy(viewModel)

        spyViewModel.loadData()
        verify(spyViewModel).toggleLoadingState(anyBoolean())
        verify(dataSource).getFavorites()
        verify(dataSource).getRestaurants()
        verify(spyViewModel).updateFavorites(anyList(), anySet())
    }

    @Test
    fun toggleLoadingState() {
        val spyViewModel = Mockito.spy(viewModel)
        val isLoading = true

        spyViewModel.toggleLoadingState(isLoading)
        assertTrue(spyViewModel.viewState.value?.showLoading == isLoading)
    }

    @Test
    fun showResults() {
        val spyViewModel = Mockito.spy(viewModel)

        spyViewModel.showResults(restaurants)
        assertTrue(spyViewModel.viewState.value?.results == restaurants)
    }

    @Test
    fun updateFavorites() {
        //using local variables so that other tests are unaffected
        val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
            3.0f,4,5.0f,5,6, 7),false)
        val restaurants = mutableListOf<Restaurant>(restaurant)
        val favorites = mutableSetOf<String>("name")

        viewModel.updateFavorites(restaurants,favorites)
        assertTrue(restaurants[0].isFavorite)
    }

    @Test
    fun updateSingleFavorite() {
        val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
            3.0f,4,5.0f,5,6, 7),false)
        val restaurants = mutableListOf<Restaurant>(restaurant)

        viewModel.updateSingleFavorite(restaurants,restaurant)
        assertTrue(restaurants[0].isFavorite)
    }

    @Test
    fun listItemClicked_NonFavorite() {
        val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
            3.0f,4,5.0f,5,6, 7),false)

        val spyViewModel = Mockito.spy(viewModel)

        spyViewModel.listItemClicked(restaurant)
        verify(dataSource).addToFavorites(restaurant)
        verify(spyViewModel).updateSingleFavorite(anyList(),eq(restaurant))
        verify(spyViewModel).showResults(anyList())
    }

    @Test
    fun listItemClicked_Favorite() {
        val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
            3.0f,4,5.0f,5,6, 7),true)
        val spyViewModel = Mockito.spy(viewModel)

        spyViewModel.listItemClicked(restaurant)
        verify(dataSource).removeFromFavorites(restaurant)
        verify(spyViewModel).updateSingleFavorite(anyList(),eq(restaurant))
        verify(spyViewModel).showResults(anyList())
    }

    @Test
    fun onSearchQueryChanged() {
        val spyViewModel = Mockito.spy(viewModel)
        val query = "query"

        spyViewModel.onSearchQueryChanged(query)
        verify(spyViewModel).showResults(anyList())
        verify(spyViewModel).searchFilterRestaurants(anyList(),eq(query))
    }

    @Test
    fun onSortTypeChanged() {
        val position = 3
        val spyViewModel = Mockito.spy(viewModel)

        spyViewModel.onSortTypeChanged(position)
        verify(spyViewModel).sortRestaurants(anyList())
        verify(spyViewModel).showResults(anyList())
    }

    @Test
    fun sortRestaurants() {
        viewModel.onSortTypeChanged(0)

        viewModel.sortRestaurants(restaurants)
        assertTrue(viewModel.restaurants[0].name.equals(restaurant2.name))
    }

    @Test
    fun searchFilterRestaurants_ValidQuery() {
        val validQuery = restaurants[0].name!!

        val filteredList = viewModel.searchFilterRestaurants(restaurants, validQuery)

        assertTrue(filteredList.size > 0)
    }


    @Test
    fun searchFilterRestaurants_InvalidQuery() {
        val invalidQuery = "$$$$$$$$"

        val filteredList = viewModel.searchFilterRestaurants(restaurants, invalidQuery)

        assertEquals(0, filteredList.size.toLong())
    }

    @Test
    fun searchFilterRestaurants_EmptyQuery() {
        val emptyQuery = ""

        val filteredList = viewModel.searchFilterRestaurants(restaurants, emptyQuery)

        assertEquals(filteredList.size.toLong(), restaurants.size.toLong())
    }
}
package com.stoyanoff.secretshop.presentation.restaurants

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.stoyanoff.secretshop.data.model.OpenStatus
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.data.model.SortingValues
import com.stoyanoff.secretshop.inject.localDataModule
import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsRepository.Companion.FAVORITES_KEY
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by L on 31/07/2019.
 * Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsRepositoryTest : KoinTest {

    @Mock
    private lateinit var context : Context
    @Mock
    private lateinit var editor : SharedPreferences.Editor

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var repository: RestaurantsRepository
    val restaurant = Restaurant("name",OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
        3.0f,4,5.0f,5,6, 7),false)
    val restaurants = mutableListOf<Restaurant>(restaurant)

    @Before
    fun setUp() {
        startKoin(listOf(localDataModule))
        MockitoAnnotations.initMocks(this)
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        this.repository = RestaurantsRepository(this.context)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.commit()).thenReturn(true)

     }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun addToFavorites() {
        //FIXME koin injection of context causes a crash
        Mockito.`when`(editor.putStringSet(eq(FAVORITES_KEY), any())).thenReturn(editor)
        repository.addToFavorites(restaurant)

        verify(sharedPreferences).getString(anyString(),any())
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(eq( FAVORITES_KEY),any())
        verify(editor).commit()
    }

    @Test
    fun removeFromFavorites() {
    }

    @Test
    fun getFavorites() {
    }

    @Test
    fun getRestaurants() {
    }
}
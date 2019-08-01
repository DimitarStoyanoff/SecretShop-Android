package com.stoyanoff.secretshop.presentation.restaurants

import android.content.SharedPreferences
import android.content.res.AssetManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.stoyanoff.secretshop.data.model.OpenStatus
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.data.model.SortingValues
import com.stoyanoff.secretshop.inject.dataModule
import com.stoyanoff.secretshop.inject.localDataModule
import com.stoyanoff.secretshop.inject.presentationModule
import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsRepository.Companion.FAVORITES_KEY
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream


/**
 * Created by L on 31/07/2019.
 * Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsRepositoryTest : KoinTest {

    @Mock
    private lateinit var editor : SharedPreferences.Editor

    private val assetManager : AssetManager by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val repository: RestaurantsDataSource by inject()
    val restaurant = Restaurant("name", OpenStatus.CLOSED, SortingValues(1.0f,2.0f,
        3.0f,4,5.0f,5,6, 7),false)
    val restaurants = mutableListOf<Restaurant>(restaurant)

    @Before
    fun setUp() {
        startKoin { modules(listOf(dataModule, localDataModule)) }
        declareMock<SharedPreferences>()
        declareMock<AssetManager>()
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(editor)
        Mockito.`when`(editor.commit()).thenReturn(true)

     }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun addToFavorites() {
        Mockito.`when`(editor.putStringSet(eq(FAVORITES_KEY), any())).thenReturn(editor)
        repository.addToFavorites(restaurant)

        verify(sharedPreferences).getStringSet(anyString(),any())
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(eq( FAVORITES_KEY),any())
        verify(editor).commit()
    }

    @Test
    fun removeFromFavorites() {
        //Couldn't think of a way to test the actual .add()/.remove() from the list without doing
        // unnecessary refactoring to the tested code, so line-by-line testing should suffice
        Mockito.`when`(editor.putStringSet(eq(FAVORITES_KEY), any())).thenReturn(editor)
        repository.removeFromFavorites(restaurant)

        verify(sharedPreferences).getStringSet(anyString(),any())
        verify(sharedPreferences).edit()
        verify(editor).putStringSet(eq( FAVORITES_KEY),any())
        verify(editor).commit()
    }

    @Test
    fun getFavorites() {
        repository.getFavorites()

        verify(sharedPreferences).getStringSet(eq(FAVORITES_KEY), any())
    }
}
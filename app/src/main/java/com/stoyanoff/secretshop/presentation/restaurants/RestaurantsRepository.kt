package com.stoyanoff.secretshop.presentation.restaurants

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.data.model.RestaurantsResponse
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsRepository(
    private val assetManager : AssetManager
) : RestaurantsDataSource, KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()

    override fun addToFavorites(restaurant: Restaurant) {

        restaurant.name?.let {
            val favoritesSet: MutableSet<String> = HashSet<String>(
                sharedPreferences.getStringSet(FAVORITES_KEY, HashSet<String>()))
            favoritesSet.add(it)
            val editor = sharedPreferences.edit()
            editor.putStringSet(FAVORITES_KEY, favoritesSet)
            editor.commit()
        }
    }

    override fun removeFromFavorites(restaurant: Restaurant) {
        restaurant.name?.let {
            val favoritesSet: MutableSet<String> = HashSet<String>(
                sharedPreferences.getStringSet(FAVORITES_KEY, HashSet<String>()))
            favoritesSet.remove(it)
            val editor = sharedPreferences.edit()
            editor.putStringSet(FAVORITES_KEY, favoritesSet)
            editor.commit()
        }
    }

    override fun getFavorites(): MutableSet<String> {
        return HashSet<String>(sharedPreferences.getStringSet(FAVORITES_KEY, HashSet<String>()))
    }

    override fun getRestaurants() : MutableList<Restaurant> {
        val jsonfile: String = assetManager.open("restaurants.json").bufferedReader().use {it.readText()}
        val jsonObject = JSONObject(jsonfile)
        val restaurants = Gson().fromJson<RestaurantsResponse>(jsonObject.toString(),
            RestaurantsResponse::class.java)
        return restaurants.restaurants!!
    }

    companion object {
        const val FAVORITES_KEY = "favorites_key"
    }

}
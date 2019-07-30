package com.stoyanoff.secretshop.presentation.restaurants

import android.content.Context
import com.google.gson.Gson
import com.stoyanoff.secretshop.data.model.Restaurant
import com.stoyanoff.secretshop.data.model.RestaurantsResponse
import org.json.JSONObject

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class RestaurantsRepository(
    private val context : Context
) : RestaurantsDataSource {

    override fun getRestaurants() : MutableList<Restaurant> {
        val gson = Gson()
        val jsonfile: String = context.assets.open("restaurants.json").bufferedReader().use {it.readText()}
        val jsonObject = JSONObject(jsonfile)
        val restaurants = gson.fromJson<RestaurantsResponse>(jsonObject.toString(),
            RestaurantsResponse::class.java)
        return restaurants.restaurants!!
    }

}
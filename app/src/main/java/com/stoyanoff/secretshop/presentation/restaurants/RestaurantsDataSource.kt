package com.stoyanoff.secretshop.presentation.restaurants

import com.stoyanoff.secretshop.data.model.Restaurant

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
interface RestaurantsDataSource{
    fun getRestaurants() : MutableList<Restaurant>
}
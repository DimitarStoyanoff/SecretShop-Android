package com.stoyanoff.secretshop.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by L on 30/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
data class RestaurantsResponse(
    @SerializedName("restaurants")
    val restaurants : MutableList<Restaurant>?
)
package com.stoyanoff.secretshop.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by L on 29/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
data class SortingValues(
    @SerializedName("bestMatch")
    val bestMatch : Float?,
    @SerializedName("newest")
    val newest : Float?,
    @SerializedName("ratingAverage")
    val ratingAverage : Float?,
    @SerializedName("distance")
    val distance : Int?,
    @SerializedName("popularity")
    val popularity : Float?,
    @SerializedName("averageProductPrice")
    val averageProductPrice : Int?,
    @SerializedName("deliveryCosts")
    val deliveryCosts : Int?,
    @SerializedName("minCost")
    val minCost : Int?
    //TODO question currency types
)
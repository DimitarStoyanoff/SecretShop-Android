package com.stoyanoff.secretshop.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by L on 29/07/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
data class Restaurant (
    @SerializedName("name")
    val name : String?,
    @SerializedName("status")
    val status : OpenStatus?,
    @SerializedName("sortingValues")
    val sortingValues : SortingValues?,
    val isFavorite : Boolean = false
)


enum class OpenStatus (val status: String){
    @SerializedName("open")
    OPEN("open"),
    @SerializedName("order ahead")
    ORDER_AHEAD("order ahead"),
    @SerializedName("closed")
    CLOSED("closed")
}
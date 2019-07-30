package com.stoyanoff.secretshop.inject

import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsDataSource
import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsRepository
import org.koin.dsl.module.module

/**
 * Created by L on 29/05/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
val dataModule = module {
    factory<RestaurantsDataSource> { RestaurantsRepository(get()) }
}
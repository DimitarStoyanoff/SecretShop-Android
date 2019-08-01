package com.stoyanoff.secretshop.inject

import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsAdapter
import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsViewModel
import com.stoyanoff.secretshop.presentation.restaurants.RestaurantsViewState
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by L on 28/05/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
val presentationModule = module {

    viewModel { RestaurantsViewModel(get(), get()) }
    factory { RestaurantsViewState() }
    factory { RestaurantsAdapter() }

}
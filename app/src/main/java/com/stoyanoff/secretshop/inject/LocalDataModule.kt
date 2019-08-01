package com.stoyanoff.secretshop.inject

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by L on 30/05/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
val localDataModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(get()) as SharedPreferences }
    single<AssetManager> { get<Context>().assets }
}
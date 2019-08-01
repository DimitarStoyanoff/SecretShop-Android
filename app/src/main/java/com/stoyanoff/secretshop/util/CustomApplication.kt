package com.stoyanoff.secretshop.util

import android.app.Application
import com.stoyanoff.secretshop.inject.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by L on 27/05/2019.
 *  Copyright (c) 2017 Centroida. All rights reserved.
 */
class CustomApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        initDependencyInjectionFramework()
    }

    private fun initDependencyInjectionFramework() {
        startKoin {
            androidContext(this@CustomApplication)
            modules(listOf(dataModule, localDataModule, presentationModule))
        }
    }
}
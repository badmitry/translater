package com.badmitry.translater.app

import android.app.Application
import com.badmitry.translater.di.application
import com.badmitry.translater.di.historyScreen
import com.badmitry.translater.di.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
//            modules(listOf(application, mainScreen, historyScreen))
        }
    }
}
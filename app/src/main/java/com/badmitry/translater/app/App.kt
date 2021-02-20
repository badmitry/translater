package com.badmitry.translater.app

import android.app.Application
import com.badmitry.translater.di.application
import com.badmitry.translater.di.mainScreen
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen))
        }
    }
}
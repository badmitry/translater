package com.badmitry.translater.app

import android.app.Application
import com.badmitry.translater.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

//    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
//        return dispatchingAndroidInjector
//    }

    override fun androidInjector(): DispatchingAndroidInjector<Any>? {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}
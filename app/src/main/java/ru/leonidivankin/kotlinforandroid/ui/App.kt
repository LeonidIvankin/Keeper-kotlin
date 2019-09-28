package ru.leonidivankin.kotlinforandroid.ui

import android.app.Application
import ru.leonidivankin.kotlinforandroid.BuildConfig
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()


        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        } else {
            //product
        }



    }
}
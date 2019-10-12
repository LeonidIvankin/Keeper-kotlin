package ru.leonidivankin.kotlinforandroid.ui

import android.app.Application
import org.koin.android.ext.android.startKoin
import ru.leonidivankin.kotlinforandroid.BuildConfig
import ru.leonidivankin.kotlinforandroid.di.appModule
import ru.leonidivankin.kotlinforandroid.di.mainModule
import ru.leonidivankin.kotlinforandroid.di.noteModule
import ru.leonidivankin.kotlinforandroid.di.splashModule
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
        } else {
            //product
        }


    }
}
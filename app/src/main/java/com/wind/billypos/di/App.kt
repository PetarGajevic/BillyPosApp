package com.wind.billypos.di

import com.wind.billypos.BuildConfig
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        AndroidThreeTen.init(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(repositoryModule, viewModelModule, retrofitModule, apiModule, preferencesModule, databaseModule))
        }
    }






}
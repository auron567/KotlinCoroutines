package com.example.kotlincoroutines.app

import android.app.Application
import com.example.kotlincoroutines.BuildConfig
import com.example.kotlincoroutines.di.appModule
import com.example.kotlincoroutines.di.databaseModule
import com.example.kotlincoroutines.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule, networkModule, databaseModule)
        }

        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
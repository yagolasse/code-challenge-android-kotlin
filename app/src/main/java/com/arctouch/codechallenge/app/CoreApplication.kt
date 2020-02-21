package com.arctouch.codechallenge.app

import android.app.Application
import com.arctouch.codechallenge.dependencyinjection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CoreApplication)
            modules(appModule)
        }
    }
}
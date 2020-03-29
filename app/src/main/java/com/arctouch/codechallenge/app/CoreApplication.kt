package com.arctouch.codechallenge.app

import android.app.Application
import com.arctouch.codechallenge.dependencyinjection.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
class CoreApplication : Application() {

    private val moduleList by lazy(LazyThreadSafetyMode.NONE) {
        listOf(networkModule, daoModule, repositoryModule, dataSourceModule, viewModelModule, utilsModule)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CoreApplication)
            modules(moduleList)
        }
    }
}
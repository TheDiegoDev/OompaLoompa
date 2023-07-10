package com.example.oompaloompa.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class KoinApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApp)
            modules(apiModule,
                    repositoryModule,
                    viewModelModule,
                    roomModule)
        }
    }
}
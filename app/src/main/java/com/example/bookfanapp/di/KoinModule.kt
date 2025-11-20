package com.example.bookfanapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule,
                DispatchModule.dispatcherModule,
                NetworkModule.networkModule,
                RepositoryModule.repositoryModule,
                ViewModelModule.viewModelModule
            )
        }

    }
}
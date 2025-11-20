package com.example.bookfanapp.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DispatchModule {
    val dispatcherModule = module {
        single<CoroutineDispatcher>(named("IoDispatcher")) { Dispatchers.IO }
    }
}
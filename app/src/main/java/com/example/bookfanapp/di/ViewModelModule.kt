package com.example.bookfanapp.di

import com.example.bookfanapp.presentation.bookDetailsScreen.BookDetailsViewModel
import com.example.bookfanapp.presentation.myBooksScreen.MyBooksViewModel
import com.example.bookfanapp.presentation.searchBooksScreen.BookListViewModel
import com.example.bookfanapp.presentation.sharedViewModel.SharedBookViewModel
import com.example.bookfanapp.presentation.trendingBooksScreen.TrendingBooksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel {
            BookListViewModel(
                get(named("IoDispatcher")),
                get(),

                )
        }
        viewModel {
            BookDetailsViewModel(
                get(named("IoDispatcher")),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            MyBooksViewModel(
                get(named("IoDispatcher")),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            SharedBookViewModel()
        }
        viewModel {
           TrendingBooksViewModel(
                get(named("IoDispatcher")),
                get()
            )
        }
    }
}
package com.example.bookfanapp.di

import com.example.bookfanapp.ui.view_models.book_details.BookDetailsViewModel
import com.example.bookfanapp.ui.view_models.my_books.MyBooksViewModel
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksViewModel
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ViewModelModule {
    val viewModelModule = module {
        viewModel {
            SearchBooksViewModel(
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
           TrendingBooksViewModel(
                get(named("IoDispatcher")),
                get()
            )
        }
    }
}
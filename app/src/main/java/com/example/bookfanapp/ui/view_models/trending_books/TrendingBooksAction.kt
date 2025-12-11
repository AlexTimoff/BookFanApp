package com.example.bookfanapp.ui.view_models.trending_books

import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.ErrorStatus

sealed class TrendingBooksAction {
    data object FetchTrendingBooks : TrendingBooksAction()
    data object FetchMoreTrendingBooks : TrendingBooksAction()
    data class LoadSuccess (val bookResponse: BookResponse,val offset: Int): TrendingBooksAction()
    data class LoadError (val errorStatus: ErrorStatus): TrendingBooksAction()
}
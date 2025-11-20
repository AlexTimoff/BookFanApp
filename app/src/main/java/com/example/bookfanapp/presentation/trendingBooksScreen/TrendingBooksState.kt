package com.example.bookfanapp.presentation.trendingBooksScreen

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.ErrorStatus

data class TrendingBooksState(
    val bookQuery: String="",
    val errorStatus: ErrorStatus = ErrorStatus.NO_ERROR,
    val bookResponse: BookResponse?=null,
    val bookList: List<BookItem>? = emptyList(),
    val currentPosition: Int = 0,
    val isLoadingPage: Boolean=false,
)
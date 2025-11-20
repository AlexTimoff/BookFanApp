package com.example.bookfanapp.presentation.myBooksScreen

import com.example.bookfanapp.domain.entities.BookItem

data class MyBooksState(
    val myBookList: List<BookItem> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean=false,
)
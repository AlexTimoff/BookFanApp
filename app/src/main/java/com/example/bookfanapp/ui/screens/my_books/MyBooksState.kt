package com.example.bookfanapp.ui.screens.my_books

import com.example.bookfanapp.domain.entities.BookItem

data class MyBooksState(
    val myBookList: List<BookItem> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean=false,
)
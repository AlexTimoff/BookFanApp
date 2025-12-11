package com.example.bookfanapp.ui.screens.my_books

import com.example.bookfanapp.domain.entities.BookItem

data class MyBooksState(
    val isLoading: Boolean=false,
    val myBookList: List<BookItem> = emptyList(),
    val myBooksError: String? = null,
    val deleteError: String? = null,
)
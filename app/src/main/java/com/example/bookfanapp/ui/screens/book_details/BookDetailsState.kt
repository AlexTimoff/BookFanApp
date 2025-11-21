package com.example.bookfanapp.ui.screens.book_details

import com.example.bookfanapp.domain.entities.BookItem

data class BookDetailsState (
    val bookItem: BookItem?=null,
    val error: String? = null,
    val favouriteStatus: Boolean=false,
)
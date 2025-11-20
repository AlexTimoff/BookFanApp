package com.example.bookfanapp.presentation.bookDetailsScreen

import com.example.bookfanapp.domain.entities.BookItem

data class BookDetailsState (
    val bookItem: BookItem?=null,
    val error: String? = null,
    val favouriteStatus: Boolean=false,
)
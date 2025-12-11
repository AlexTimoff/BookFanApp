package com.example.bookfanapp.ui.screens.book_details

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus

data class BookDetailsState(
    val bookItem: BookItem? = null,
    val errorDetailsStatus: ErrorStatus = ErrorStatus.NO_ERROR,
    val favouriteStatus: Boolean = false,
    val checkFavouriteStatusError: String? = null,
    val changeFavouriteStatusError: String? = null,
)
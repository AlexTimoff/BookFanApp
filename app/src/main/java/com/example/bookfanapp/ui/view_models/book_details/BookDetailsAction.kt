package com.example.bookfanapp.ui.view_models.book_details

import com.example.bookfanapp.domain.entities.BookItem

sealed class BookDetailsAction {
    data class LoadDetails(val bookItem: BookItem) : BookDetailsAction()
    data class ChangeFavouriteStatus (val bookItem: BookItem) : BookDetailsAction()
    data class CheckFavouriteStatus (val keyBook: String) : BookDetailsAction()
}
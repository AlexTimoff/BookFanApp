package com.example.bookfanapp.ui.view_models.book_details

import com.example.bookfanapp.domain.entities.BookItem

sealed class BookDetailsIntent {
    data class LoadDetails(val bookItem: BookItem) : BookDetailsIntent()
    data class ChangeFavouriteStatus (val bookItem: BookItem) : BookDetailsIntent()
    data class CheckFavouriteStatus (val keyBook: String) : BookDetailsIntent()
}
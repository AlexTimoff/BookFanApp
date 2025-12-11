package com.example.bookfanapp.ui.view_models.book_details

import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus

sealed class BookDetailsAction {
    data class ChooseBook (val bookItem: BookItem) :BookDetailsAction()

    data class FetchDetails(val bookItem: BookItem) : BookDetailsAction()
    data class LoadSuccessDetails (val bookDetails: BookDetails): BookDetailsAction()
    data class LoadErrorDetails (val errorStatus: ErrorStatus): BookDetailsAction()

    data class CheckFavouriteStatus (val keyBook: String): BookDetailsAction()
    data class LoadSuccessFavouriteStatus (val status: Boolean): BookDetailsAction()
    data class LoadErrorFavouriteStatus (val error: String): BookDetailsAction()

    data class ChangeFavouriteStatus (val bookItem: BookItem) : BookDetailsAction()

    data object SuccessAdded: BookDetailsAction()
    data class LoadErrorAdded (val error: String): BookDetailsAction()

    data object SuccessDeleted: BookDetailsAction()
    data class LoadErrorDeleted (val error: String): BookDetailsAction()

}
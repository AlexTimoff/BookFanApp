package com.example.bookfanapp.ui.view_models.search_books

import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.ErrorStatus

sealed class SearchBooksAction {
    data object Initial : SearchBooksAction()
    data class UpdateBookQuery(val bookQuery: String) : SearchBooksAction()
    data class FetchBooks (val query: String)  : SearchBooksAction()
    data class FetchMoreBooks (val query: String) : SearchBooksAction()
    data class LoadSuccess (val bookResponse: BookResponse, val offset: Int): SearchBooksAction()
    data class LoadError (val errorStatus: ErrorStatus): SearchBooksAction()
    data object ResetQuery : SearchBooksAction()
}
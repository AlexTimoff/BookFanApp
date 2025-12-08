package com.example.bookfanapp.ui.view_models.search_books

sealed class SearchBooksAction {
    data object Initial : SearchBooksAction()
    data class UpdateBookQuery(val bookQuery: String) : SearchBooksAction()
    data class LoadBookList(val name: String, val isFirstLoad: Boolean) : SearchBooksAction()
    data object ResetQuery : SearchBooksAction()
}
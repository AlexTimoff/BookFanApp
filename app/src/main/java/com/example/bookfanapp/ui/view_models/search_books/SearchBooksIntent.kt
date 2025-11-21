package com.example.bookfanapp.ui.view_models.search_books

sealed class SearchBooksIntent {
    data object Initial : SearchBooksIntent()
    data class UpdateBookQuery(val bookQuery: String) : SearchBooksIntent()
    data class LoadBookList(val name: String, val isFirstLoad: Boolean) : SearchBooksIntent()
    data object ResetQuery : SearchBooksIntent()
}
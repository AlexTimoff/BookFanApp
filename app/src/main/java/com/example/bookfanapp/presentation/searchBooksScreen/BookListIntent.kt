package com.example.bookfanapp.presentation.searchBooksScreen

sealed class BookListIntent {
    data object Initial : BookListIntent()
    data class UpdateBookQuery(val bookQuery: String) : BookListIntent()
    data class LoadBookList(val name: String, val isFirstLoad: Boolean) : BookListIntent()
    data object ResetQuery : BookListIntent()
}
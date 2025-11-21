package com.example.bookfanapp.ui.view_models.my_books

sealed class MyBooksIntent {
    data object ShowMyBooks : MyBooksIntent()
    data class DeleteMyBook(val keyBook: String) : MyBooksIntent()
}
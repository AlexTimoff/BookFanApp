package com.example.bookfanapp.presentation.myBooksScreen

sealed class MyBooksIntent {
    data object ShowMyBooks : MyBooksIntent()
    data class DeleteMyBook(val keyBook: String) : MyBooksIntent()
}
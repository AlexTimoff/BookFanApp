package com.example.bookfanapp.ui.view_models.my_books

sealed class MyBooksAction {
    data object ShowMyBooks : MyBooksAction()
    data class DeleteMyBook(val keyBook: String) : MyBooksAction()
}
package com.example.bookfanapp.ui.view_models.my_books

import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction

sealed class MyBooksAction {
    data object ShowMyBooks : MyBooksAction()
    data class LoadMyBooks (val myBooks: List<BookItem>):  MyBooksAction()
    data class LoadError (val error: String):  MyBooksAction()

    data class DeleteMyBook(val keyBook: String) : MyBooksAction()
    data object SuccessDeleted: MyBooksAction()
    data class LoadErrorDeleted (val error: String): MyBooksAction()
}
package com.example.bookfanapp.domain.repositories

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction
import com.example.bookfanapp.ui.view_models.my_books.MyBooksAction

interface DatabaseRepository {
    suspend fun getAllFavourites(): MyBooksAction

    suspend fun <T> addFavouriteBook(
        bookItem: BookItem,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T


    suspend fun <T> deleteFavouriteBook(
        keyBook: String,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T

    suspend fun checkFavouriteStatus(keyBook: String): BookDetailsAction
}
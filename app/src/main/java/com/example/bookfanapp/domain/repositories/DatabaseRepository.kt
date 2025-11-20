package com.example.bookfanapp.domain.repositories

import com.example.bookfanapp.domain.entities.BookItem

interface DatabaseRepository {
    suspend fun getAllFavourites(): List<BookItem>

    suspend fun addFavouriteBook(bookItem: BookItem)

    suspend fun deleteFavouriteBook(keyBook: String)

    suspend fun checkFavouriteStatus(keyBook: String): Boolean
}
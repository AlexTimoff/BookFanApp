package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.repositories.DatabaseRepository

class AddFavouriteBookUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun <T> invoke(
        bookItem: BookItem,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T {
        return repo.addFavouriteBook(bookItem, createSuccessAction, createErrorAction)
    }
}
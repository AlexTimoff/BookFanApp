package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.repositories.DatabaseRepository

class DeleteFavouriteBookUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun <T> invoke(
        keyBook: String,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T {
        return repo.deleteFavouriteBook(keyBook,createSuccessAction,createErrorAction)
    }
}

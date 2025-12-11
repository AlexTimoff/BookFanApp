package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.repositories.DatabaseRepository
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction

class CheckFavouriteStatusUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun invoke(keyBook: String): BookDetailsAction {
        return repo.checkFavouriteStatus(keyBook)
    }
}
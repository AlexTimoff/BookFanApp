package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.repositories.DatabaseRepository

class DeleteFavouriteBookUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun invoke(keyBook: String){
        repo.deleteFavouriteBook(keyBook)
    }
}

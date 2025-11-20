package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.repositories.DatabaseRepository

class GetFavouritesUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun invoke():List<BookItem>{
        return repo.getAllFavourites()
    }
}
package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.repositories.DatabaseRepository
import com.example.bookfanapp.ui.view_models.my_books.MyBooksAction

class GetFavouritesUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun invoke():MyBooksAction{
        return repo.getAllFavourites()
    }
}
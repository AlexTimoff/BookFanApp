package com.example.bookfanapp.domain.useCases.database

import com.example.bookfanapp.domain.repositories.DatabaseRepository

class CheckFavouriteStatusUseCase(
    private val repo: DatabaseRepository
) {
    suspend operator fun invoke(keyBook: String):Boolean{
        return repo.checkFavouriteStatus(keyBook)
    }
}
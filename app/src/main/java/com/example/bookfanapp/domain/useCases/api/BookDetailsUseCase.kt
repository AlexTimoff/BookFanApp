package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.repositories.BookRepository

class BookDetailsUseCase(
    private val repo: BookRepository
) {
    suspend operator fun invoke(keyBook: String): BookDetails? {
        return repo.fetchBookDetails(keyBook)
    }
}
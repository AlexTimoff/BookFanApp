package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.repositories.BookRepository

class BooksListUseCase(
    private val repo: BookRepository
) {
    suspend operator fun invoke(request: String, offset: Int, limit: Int): Result<BookResponse?> {
        return repo.fetchBooksList(request, offset, limit)
    }
}
package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.repositories.BookRepository

class TrendingBooksListUseCase (
    private val repo: BookRepository
) {
    suspend operator fun invoke(request: String, offset: Int, limit: Int): Result<BookResponse?> {
        return repo.fetchSubjectBooksList(request, offset, limit)
    }
}
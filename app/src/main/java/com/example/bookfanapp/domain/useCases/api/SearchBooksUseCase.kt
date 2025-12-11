package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksAction

class SearchBooksUseCase(
    private val repo: BookRepository
) {
    suspend operator fun invoke(request: String, offset: Int, limit: Int): SearchBooksAction {
        return repo.fetchBooksList(request, offset, limit)
    }
}
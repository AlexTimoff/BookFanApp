package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction

class BookDetailsUseCase(
    private val repo: BookRepository
) {
    suspend operator fun invoke(keyBook: String): BookDetailsAction {
        return repo.fetchBookDetails(keyBook)
    }
}
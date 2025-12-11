package com.example.bookfanapp.domain.useCases.api

import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksAction

class TrendingBooksListUseCase (
    private val repo: BookRepository
) {
    suspend operator fun invoke(request: String, offset: Int, limit: Int): TrendingBooksAction {
        return repo.fetchTrendingBooksList(request, offset, limit)
    }
}
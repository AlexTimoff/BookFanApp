package com.example.bookfanapp.domain.repositories

import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksAction
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksAction

interface BookRepository {
    suspend fun fetchBooksList (request: String, offset: Int, limit: Int): SearchBooksAction
    suspend fun fetchTrendingBooksList (request: String, offset: Int, limit: Int): TrendingBooksAction
    suspend fun fetchBookDetails(keyBook: String): BookDetailsAction
}
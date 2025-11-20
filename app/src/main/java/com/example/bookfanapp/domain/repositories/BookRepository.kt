package com.example.bookfanapp.domain.repositories

import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.entities.BookResponse

interface BookRepository {
    suspend fun fetchBooksList (request: String, offset: Int, limit: Int): Result<BookResponse?>
    suspend fun fetchSubjectBooksList (request: String, offset: Int, limit: Int): Result<BookResponse?>
    suspend fun fetchBookDetails(keyBook: String): BookDetails?
}
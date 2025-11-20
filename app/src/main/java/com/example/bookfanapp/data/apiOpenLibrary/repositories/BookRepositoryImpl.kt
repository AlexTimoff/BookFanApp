package com.example.bookfanapp.data.apiOpenLibrary.repositories

import com.example.bookfanapp.data.apiOpenLibrary.network.BookApiService
import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookDetailsDto
import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookResponseDto
import com.example.bookfanapp.data.apiOpenLibrary.network.mapper.DtoToBookDetailsMapper
import com.example.bookfanapp.data.apiOpenLibrary.network.mapper.DtoToBookListMapper
import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.FetchBooksError
import com.example.bookfanapp.domain.repositories.BookRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class BookRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val apiService: BookApiService,
    private val dtoToBookListMapper: DtoToBookListMapper,
    private val dtoToBookDetailsMapper: DtoToBookDetailsMapper
) : BookRepository {

    override suspend fun fetchBooksList(
        request: String,
        offset: Int,
        limit: Int
    ): Result<BookResponse?> {
        return withContext(ioDispatcher) {
            try {
                val listBooksResponse = apiService.getBooksList(request, offset, limit)
                if (listBooksResponse.status.isSuccess()) {
                    val result = dtoToBookListMapper(listBooksResponse.body<BookResponseDto>())
                    Result.success(result)
                } else {
                    Result.failure(FetchBooksError.ServerError(listBooksResponse.status.value.toString()))
                }
            } catch (e: IOException) {
                Result.failure(FetchBooksError.NetworkError(e.message))
            } catch (e: Exception) {
                Result.failure(FetchBooksError.UnknownError(e.message))
            }
        }
    }

    override suspend fun fetchSubjectBooksList(
        request: String,
        offset: Int,
        limit: Int
    ): Result<BookResponse?> {
        return withContext(ioDispatcher) {
            try {
                val subjectBooksListResponse = apiService.getSubjectBooks(request, offset, limit)
                if (subjectBooksListResponse.status.isSuccess()) {
                    val result = dtoToBookListMapper(subjectBooksListResponse.body<BookResponseDto>())
                    Result.success(result)
                } else {
                    Result.failure(FetchBooksError.ServerError(subjectBooksListResponse.status.value.toString()))
                }
            } catch (e: IOException) {
                Result.failure(FetchBooksError.NetworkError(e.message))
            } catch (e: Exception) {
                Result.failure(FetchBooksError.UnknownError(e.message))
            }
        }
    }

    override suspend fun fetchBookDetails(keyBook: String): BookDetails? {
        return withContext(ioDispatcher) {
            try {
                val bookDetailsResponse = apiService.getBookDetailsInfo(keyBook)
                if (bookDetailsResponse.status.isSuccess()) {
                    dtoToBookDetailsMapper(bookDetailsResponse.body<BookDetailsDto>())
                } else {
                    null
                }
            } catch (e: IOException) {
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

package com.example.bookfanapp.data.api_open_library.repositories

import com.example.bookfanapp.data.api_open_library.network.BookApiService
import com.example.bookfanapp.data.api_open_library.network.entities.BookDetailsDto
import com.example.bookfanapp.data.api_open_library.network.entities.BookResponseDto
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookDetailsMapper
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookListMapper
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksAction
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksAction
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.io.IOException

class BookRepositoryImpl(
    private val apiService: BookApiService,
    private val dtoToBookListMapper: DtoToBookListMapper,
    private val dtoToBookDetailsMapper: DtoToBookDetailsMapper
) : BookRepository {

    override suspend fun fetchBooksList(
        request: String,
        offset: Int,
        limit: Int
    ): SearchBooksAction {
        try {
            val listBooksResponse = apiService.getBooksList(request, offset, limit)
            if (listBooksResponse.status.isSuccess()) {
                val result = dtoToBookListMapper(listBooksResponse.body<BookResponseDto>())
                return if (result.docs.isNullOrEmpty()){
                    SearchBooksAction.LoadError(ErrorStatus.NOTHING_FOUND)
                }else{
                    SearchBooksAction.LoadSuccess(result,offset)
                }
            }else {
                return SearchBooksAction.LoadError(ErrorStatus.SERVER_ERROR)
            }
        } catch (e: IOException) {
            return SearchBooksAction.LoadError(ErrorStatus.NETWORK_ERROR)
        } catch (e: Exception) {
            return SearchBooksAction.LoadError(ErrorStatus.UNKNOWN_ERROR)
        }
    }

    override suspend fun fetchTrendingBooksList(
        request: String,
        offset: Int,
        limit: Int
    ): TrendingBooksAction {
        try {
            val trendingBooksListResponse = apiService.getTrendingBooks(request, offset, limit)
            if (trendingBooksListResponse.status.isSuccess()) {
                val result = dtoToBookListMapper(trendingBooksListResponse.body<BookResponseDto>())
                return if (result.docs.isNullOrEmpty()){
                    TrendingBooksAction.LoadError(ErrorStatus.NOTHING_FOUND)
                }else{
                    TrendingBooksAction.LoadSuccess(result,offset)
                }
            } else {
                return TrendingBooksAction.LoadError(ErrorStatus.SERVER_ERROR)
            }
        } catch (e: IOException) {
            return TrendingBooksAction.LoadError(ErrorStatus.NETWORK_ERROR)
        } catch (e: Exception) {
            return TrendingBooksAction.LoadError(ErrorStatus.UNKNOWN_ERROR)
        }
    }

    override suspend fun fetchBookDetails(keyBook: String): BookDetailsAction {
        try {
            val bookDetailsResponse = apiService.getBookDetailsInfo(keyBook)
            if (bookDetailsResponse.status.isSuccess()) {
                val result = dtoToBookDetailsMapper(bookDetailsResponse.body<BookDetailsDto>())
                return if (result.description.isNullOrEmpty()) {
                    BookDetailsAction.LoadErrorDetails(ErrorStatus.NOTHING_FOUND)
                }else{
                    BookDetailsAction.LoadSuccessDetails(result)
                }
            } else {
                return BookDetailsAction.LoadErrorDetails(ErrorStatus.SERVER_ERROR)
            }
        } catch (e: IOException) {
            return BookDetailsAction.LoadErrorDetails(ErrorStatus.NETWORK_ERROR)
        } catch (e: Exception) {
            return BookDetailsAction.LoadErrorDetails(ErrorStatus.UNKNOWN_ERROR)
        }
    }
}

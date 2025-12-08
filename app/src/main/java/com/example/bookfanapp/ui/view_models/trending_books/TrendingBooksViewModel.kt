package com.example.bookfanapp.ui.view_models.trending_books

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.errors.FetchBooksError
import com.example.bookfanapp.domain.useCases.api.TrendingBooksListUseCase
import com.example.bookfanapp.ui.screens.trending_books.TrendingBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TRENDING_BOOKS = "trending_score_hourly_sum:[1 TO *] -subject:\"content_warning:cover\" language:rus -subject:\"content_warning:cover\" -subject:\"content_warning:cover\""

class TrendingBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val trendingBooksListUseCase: TrendingBooksListUseCase,
) : ViewModel() {

    private val _trendingBooksState = MutableStateFlow(TrendingBooksState())
    val trendingBooksState: StateFlow<TrendingBooksState> = _trendingBooksState

    fun handleAction(action: TrendingBooksAction) {
        when (action) {
            is TrendingBooksAction.LoadTrendingBooks -> loadSubjectsBooks(action.isInitialLoad)
        }
    }

    private fun loadSubjectsBooks(
        isInitialLoad: Boolean = false,
    ) {
        val query = TRENDING_BOOKS
        val limit = 12
        val offset = if (isInitialLoad) 0 else _trendingBooksState.value.currentPosition + limit

        if (!isInitialLoad && _trendingBooksState.value.isLoadingPage) return
        if (isInitialLoad) {
            _trendingBooksState.value = _trendingBooksState.value.copy(
                bookList = emptyList(),
                currentPosition = 0,
                errorStatus = ErrorStatus.NO_ERROR,
                isLoadingPage = true
            )
        } else {
            _trendingBooksState.value = _trendingBooksState.value.copy(
                isLoadingPage = false
            )
        }
        viewModelScope.launch(ioDispatcher) {
            val result = trendingBooksListUseCase(query, offset, limit)
            result.onSuccess {
                loadBooks(result.getOrNull(), isInitialLoad, offset)
            }
            result.onFailure { error ->
                val errorMessage = when (error) {
                    is FetchBooksError.NetworkError -> ErrorStatus.NETWORK_ERROR
                    is FetchBooksError.ServerError -> ErrorStatus.SERVER_ERROR
                    is FetchBooksError.UnknownError -> ErrorStatus.UNKNOWN_ERROR
                    else -> ErrorStatus.UNKNOWN_ERROR
                }
                _trendingBooksState.value = _trendingBooksState.value.copy(
                    isLoadingPage = false,
                    errorStatus = errorMessage
                )
            }
        }
    }

    private fun loadBooks(bookResponse: BookResponse?, isInitialLoad: Boolean, offset: Int) {
        if (bookResponse != null) {
            if (!bookResponse.docs.isNullOrEmpty()) {
                val newBookList = if (isInitialLoad) {
                    bookResponse.docs
                } else {
                    _trendingBooksState.value.bookList?.plus(bookResponse.docs)
                        ?: bookResponse.docs
                }
                Log.d("TrendingBooksList", "Download $newBookList")
                _trendingBooksState.value = _trendingBooksState.value.copy(
                    bookResponse = bookResponse,
                    errorStatus = ErrorStatus.NO_ERROR,
                    bookList = newBookList,
                    currentPosition = offset,
                    isLoadingPage = false
                )
            } else {
                if (isInitialLoad) {
                    _trendingBooksState.value = _trendingBooksState.value.copy(
                        errorStatus = ErrorStatus.NO_BOOKS_ERROR,
                    )
                }
                _trendingBooksState.value = _trendingBooksState.value.copy(
                    isLoadingPage = false
                )
            }
        } else {
            if (isInitialLoad) {
                _trendingBooksState.value = _trendingBooksState.value.copy(
                    errorStatus = ErrorStatus.NO_BOOKS_ERROR,
                )
            }
            _trendingBooksState.value = _trendingBooksState.value.copy(
                isLoadingPage = false
            )
        }
        Log.d("TrendingBooksList", "ErrorStatus ${_trendingBooksState.value.errorStatus}")
    }
}
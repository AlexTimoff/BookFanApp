package com.example.bookfanapp.ui.view_models.trending_books

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.useCases.api.TrendingBooksListUseCase
import com.example.bookfanapp.ui.screens.trending_books.TrendingBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val TRENDING_BOOKS =
    "trending_score_hourly_sum:[1 TO *] -subject:\"content_warning:cover\" language:rus -subject:\"content_warning:cover\" -subject:\"content_warning:cover\""

class TrendingBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val trendingBooksListUseCase: TrendingBooksListUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<TrendingBooksAction>(extraBufferCapacity = bufferSize)

    private val _trendingBooksState = MutableStateFlow(TrendingBooksState())
    val trendingBooksState: StateFlow<TrendingBooksState> = _trendingBooksState.asStateFlow()

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { trendingBooksAction ->
                when (trendingBooksAction) {
                    is TrendingBooksAction.FetchTrendingBooks -> {
                        launch {
                            fetchBooks(isInitialLoad = true, trendingBooksAction)
                        }
                    }

                    is TrendingBooksAction.FetchMoreTrendingBooks -> {
                        launch {
                            fetchBooks(isInitialLoad = false, trendingBooksAction)
                        }
                    }

                    is TrendingBooksAction.LoadSuccess -> {
                        reduce(trendingBooksAction)
                        Log.d(
                            "TrendingBooksViewModel",
                            "booklist size ${_trendingBooksState.value.bookList?.size}, booklist ${_trendingBooksState.value.bookList}"
                        )
                    }

                    is TrendingBooksAction.LoadError -> {
                        reduce(trendingBooksAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        trendingBooksAction: TrendingBooksAction
    ) {
        when (trendingBooksAction) {
            is TrendingBooksAction.FetchTrendingBooks -> _trendingBooksState.update {
                it.copy(
                    isLoadingPage = true,
                    bookList = emptyList(),
                    currentPosition = 0,
                    errorStatus = ErrorStatus.NO_ERROR,
                )
            }

            is TrendingBooksAction.FetchMoreTrendingBooks -> _trendingBooksState.update {
                it.copy(
                    isLoadingPage = false
                )
            }

            is TrendingBooksAction.LoadSuccess -> {
                val currentList = _trendingBooksState.value.bookList ?: emptyList()
                val newList = currentList + trendingBooksAction.bookResponse.docs!!
                Log.d("TrendingBooksViewModel", "offset ${trendingBooksAction.offset.toString()}")
                _trendingBooksState.update {
                    it.copy(
                        isLoadingPage = false,
                        errorStatus = ErrorStatus.NO_ERROR,
                        bookList = newList,
                        currentPosition = trendingBooksAction.offset,
                    )
                }
            }

            is TrendingBooksAction.LoadError -> {
                if (!_trendingBooksState.value.bookList.isNullOrEmpty() && trendingBooksAction.errorStatus == ErrorStatus.NOTHING_FOUND) {
                    _trendingBooksState.update {
                        it.copy(
                            isLoadingPage = false,
                            errorStatus = ErrorStatus.NO_ERROR
                        )
                    }
                } else {
                    _trendingBooksState.update {
                        it.copy(
                            isLoadingPage = false,
                            errorStatus = trendingBooksAction.errorStatus
                        )
                    }
                }
            }
        }
    }

    fun dispatch(trendingBooksAction: TrendingBooksAction) {
        if (!actions.tryEmit(trendingBooksAction)) {
            error("Action buffer full!")
        }
    }

    private fun fetchBooks(isInitialLoad: Boolean, trendingBooksAction: TrendingBooksAction) {
        reduce(trendingBooksAction)
        val query = TRENDING_BOOKS
        val limit = 12
        val offset = if (isInitialLoad) 0 else _trendingBooksState.value.currentPosition + limit
        if (!isInitialLoad && _trendingBooksState.value.isLoadingPage) return
        viewModelScope.launch(ioDispatcher) {
            val result = trendingBooksListUseCase(query, offset, limit)
            dispatch(trendingBooksAction = result)
            Log.d("TrendingBooksViewModel", "ErrorStatus ${result.toString()}")
        }
    }
}
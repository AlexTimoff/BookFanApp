package com.example.bookfanapp.ui.view_models.search_books

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.useCases.api.SearchBooksUseCase
import com.example.bookfanapp.ui.screens.search_books.SearchBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val searchBooksUseCase: SearchBooksUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<SearchBooksAction>(extraBufferCapacity = bufferSize)

    private val _searchBooksState = MutableStateFlow(SearchBooksState())
    val searchBooksState: StateFlow<SearchBooksState> = _searchBooksState.asStateFlow()

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { searchBooksAction ->
                when (searchBooksAction) {
                    is SearchBooksAction.Initial -> {
                        reduce(searchBooksAction)
                    }

                    is SearchBooksAction.UpdateBookQuery -> {
                        reduce(searchBooksAction)
                    }

                    is SearchBooksAction.FetchBooks -> {
                        launch {
                            fetchBooks(
                                bookQuery = searchBooksAction.query,
                                isInitialLoad = true,
                                searchBooksAction = searchBooksAction
                            )
                        }
                    }

                    is SearchBooksAction.FetchMoreBooks -> {
                        launch {
                            fetchBooks(
                                bookQuery = searchBooksAction.query,
                                isInitialLoad = false,
                                searchBooksAction = searchBooksAction
                            )
                        }
                    }

                    is SearchBooksAction.LoadSuccess -> {
                        reduce(searchBooksAction)
                        Log.d(
                            "SearchBooksViewModel",
                            "Booklist_size ${_searchBooksState.value.bookList?.size}, booklist ${_searchBooksState.value.bookList}"
                        )
                    }

                    is SearchBooksAction.LoadError -> {
                        reduce(searchBooksAction)
                    }

                    is SearchBooksAction.ResetQuery -> {
                        reduce(searchBooksAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        searchBooksAction: SearchBooksAction
    ) {
        when (searchBooksAction) {
            is SearchBooksAction.Initial -> _searchBooksState.update {
                it.copy(
                    isInitialScreen = true
                )
            }

            is SearchBooksAction.UpdateBookQuery -> _searchBooksState.update {
                it.copy(
                    bookQuery = searchBooksAction.bookQuery
                )
            }

            is SearchBooksAction.FetchBooks -> _searchBooksState.update {
                it.copy(
                    isInitialScreen = false,
                    isLoadingPage = true,
                    bookList = emptyList(),
                    currentPosition = 0,
                    errorStatus = ErrorStatus.NO_ERROR,
                )
            }

            is SearchBooksAction.FetchMoreBooks -> _searchBooksState.update {
                it.copy(
                    isInitialScreen = false,
                    isLoadingPage = false
                )
            }

            is SearchBooksAction.LoadSuccess -> {
                val currentList = _searchBooksState.value.bookList ?: emptyList()
                val newList = currentList + searchBooksAction.bookResponse.docs!!
                Log.d("SearchBooksViewModel", "offset ${searchBooksAction.offset}")
                _searchBooksState.update {
                    it.copy(
                        isInitialScreen = false,
                        isLoadingPage = false,
                        errorStatus = ErrorStatus.NO_ERROR,
                        bookList = newList,
                        currentPosition = searchBooksAction.offset,
                    )
                }
            }

            is SearchBooksAction.LoadError -> {
                if (!_searchBooksState.value.bookList.isNullOrEmpty() && searchBooksAction.errorStatus == ErrorStatus.NOTHING_FOUND) {
                    _searchBooksState.update {
                        it.copy(
                            isLoadingPage = false,
                            errorStatus = ErrorStatus.NO_ERROR
                        )
                    }
                } else {
                    _searchBooksState.update {
                        it.copy(
                            isLoadingPage = false,
                            errorStatus = searchBooksAction.errorStatus
                        )
                    }
                }
            }

            is SearchBooksAction.ResetQuery -> {
                _searchBooksState.update {
                    it.copy(
                        bookQuery = "",
                        errorStatus = ErrorStatus.NO_ERROR,
                        bookList = emptyList(),
                        currentPosition = 0,
                        isLoadingPage = false,
                        isInitialScreen = true
                    )
                }
            }
        }
    }

    fun dispatch(searchBooksAction: SearchBooksAction) {
        if (!actions.tryEmit(searchBooksAction)) {
            error("Action buffer full!")
        }
    }

    private fun fetchBooks(
        bookQuery: String,
        isInitialLoad: Boolean,
        searchBooksAction: SearchBooksAction
    ) {
        reduce(searchBooksAction)
        val limit = 12
        val offset = if (isInitialLoad) 0 else _searchBooksState.value.currentPosition + limit
        if (!isInitialLoad && _searchBooksState.value.isLoadingPage) return
        viewModelScope.launch(ioDispatcher) {
            val result = searchBooksUseCase(bookQuery, offset, limit)
            dispatch(searchBooksAction = result)
            Log.d("SearchBooksViewModel", "ErrorStatus ${result.toString()}")
        }
    }
}






















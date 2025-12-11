package com.example.bookfanapp.ui.view_models.search_books

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.useCases.api.SearchBooksUseCase
import com.example.bookfanapp.ui.screens.search_books.SearchBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SearchBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val searchBooksUseCase: SearchBooksUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<SearchBooksAction>(extraBufferCapacity = bufferSize)
    var searchBooksState by mutableStateOf(SearchBooksState())
        private set

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { searchBooksAction ->
                when (searchBooksAction) {
                    is SearchBooksAction.Initial -> {
                        searchBooksState = reduce(searchBooksState, searchBooksAction)
                    }

                    is SearchBooksAction.UpdateBookQuery -> {
                        searchBooksState = reduce(searchBooksState, searchBooksAction)
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
                        searchBooksState = reduce(searchBooksState, searchBooksAction)
                        Log.d(
                            "SearchBooksViewModel",
                            "Booklist_size ${searchBooksState.bookList?.size}, booklist ${searchBooksState.bookList}"
                        )
                    }

                    is SearchBooksAction.LoadError -> {
                        searchBooksState = reduce(searchBooksState, searchBooksAction)
                    }

                    is SearchBooksAction.ResetQuery -> {
                        searchBooksState = reduce(searchBooksState, searchBooksAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        searchBooksState: SearchBooksState,
        searchBooksAction: SearchBooksAction
    ): SearchBooksState {
        return when (searchBooksAction) {
            is SearchBooksAction.Initial -> searchBooksState.copy(
                isInitialScreen = true
            )

            is SearchBooksAction.UpdateBookQuery -> searchBooksState.copy(
                bookQuery = searchBooksAction.bookQuery
            )

            is SearchBooksAction.FetchBooks -> searchBooksState.copy(
                isInitialScreen = false,
                isLoadingPage = true,
                bookList = emptyList(),
                currentPosition = 0,
                errorStatus = ErrorStatus.NO_ERROR,
            )

            is SearchBooksAction.FetchMoreBooks -> searchBooksState.copy(
                isInitialScreen = false,
                isLoadingPage = false
            )


            is SearchBooksAction.LoadSuccess -> {
                val currentList = searchBooksState.bookList ?: emptyList()
                val newList = currentList + searchBooksAction.bookResponse.docs!!
                Log.d("SearchBooksViewModel", "offset ${searchBooksAction.offset.toString()}")
                searchBooksState.copy(
                    isInitialScreen = false,
                    isLoadingPage = false,
                    errorStatus = ErrorStatus.NO_ERROR,
                    bookList = newList,
                    currentPosition = searchBooksAction.offset,
                )
            }

            is SearchBooksAction.LoadError -> {
                if (!searchBooksState.bookList.isNullOrEmpty() && searchBooksAction.errorStatus == ErrorStatus.NOTHING_FOUND) {
                    searchBooksState.copy(
                        isLoadingPage = false,
                        errorStatus = ErrorStatus.NO_ERROR
                    )
                } else {
                    searchBooksState.copy(
                        isLoadingPage = false,
                        errorStatus = searchBooksAction.errorStatus
                    )
                }
            }

            is SearchBooksAction.ResetQuery -> {
                searchBooksState.copy(
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
        searchBooksState = reduce(searchBooksState, searchBooksAction)
        val limit = 12
        val offset = if (isInitialLoad) 0 else searchBooksState.currentPosition + limit
        if (!isInitialLoad && searchBooksState.isLoadingPage) return
        viewModelScope.launch(ioDispatcher) {
            val result = searchBooksUseCase(bookQuery, offset, limit)
            dispatch(searchBooksAction = result)
            Log.d("SearchBooksViewModel", "ErrorStatus ${result.toString()}")
        }
    }
}






















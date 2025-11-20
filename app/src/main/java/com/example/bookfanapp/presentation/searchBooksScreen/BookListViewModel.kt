package com.example.bookfanapp.presentation.searchBooksScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.entities.BookResponse
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.errors.FetchBooksError
import com.example.bookfanapp.domain.useCases.api.BooksListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookListViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val booksListUseCase: BooksListUseCase,
) : ViewModel() {

    private val _bookListState = MutableStateFlow(BookListState())
    val bookListState: StateFlow<BookListState> = _bookListState

    fun handleIntent(intent: BookListIntent) {
        when (intent) {
            is BookListIntent.Initial -> isInitial()
            is BookListIntent.LoadBookList -> loadBooksList(intent.name, intent.isFirstLoad)
            is BookListIntent.UpdateBookQuery -> updateBookQuery(intent.bookQuery)
            is BookListIntent.ResetQuery -> resetQuery()
        }
    }

    private fun isInitial(){
        _bookListState.value = _bookListState.value.copy(
            isInitialScreen = true,
        )
    }


    private fun updateBookQuery(bookQuery: String) {
        _bookListState.value = _bookListState.value.copy(
            bookQuery = bookQuery,
        )
    }

    private fun loadBooksList(
        query: String,
        isFirstLoad: Boolean = false
    ) {
        _bookListState.value = _bookListState.value.copy(
            isInitialScreen = false
        )
        val limit = 10
        val offset = if (isFirstLoad) 0 else _bookListState.value.currentPosition + limit

        if (!isFirstLoad && _bookListState.value.isLoadingPage) return
        if (isFirstLoad) {
            _bookListState.value = _bookListState.value.copy(
                bookList = emptyList(),
                currentPosition = 0,
                errorStatus = ErrorStatus.NO_ERROR,
                isLoadingPage = true
            )
        } else {
            _bookListState.value = _bookListState.value.copy(
                isLoadingPage = false
            )
        }

        viewModelScope.launch(ioDispatcher) {
            val result = booksListUseCase(query, offset, limit)
            result.onSuccess {
                loadBooks(result.getOrNull(), isFirstLoad, offset)
            }
            result.onFailure { error ->
                val errorMessage = when (error) {
                    is FetchBooksError.NetworkError -> ErrorStatus.NETWORK_ERROR
                    is FetchBooksError.ServerError -> ErrorStatus.SERVER_ERROR
                    is FetchBooksError.UnknownError -> ErrorStatus.UNKNOWN_ERROR
                    else -> ErrorStatus.UNKNOWN_ERROR
                }
                _bookListState.value = _bookListState.value.copy(
                    isLoadingPage = false,
                    errorStatus = errorMessage
                )
            }
        }
    }

    private fun loadBooks(bookResponse: BookResponse?, isInitialLoad: Boolean, offset: Int) {
        Log.d("BookListViewModel", "On the position number $offset : ${bookResponse?.docs?.size}")
        if (bookResponse != null) {
            if (!bookResponse.docs.isNullOrEmpty()) {
                val newBookList = if (isInitialLoad) {
                    bookResponse.docs
                } else {
                    _bookListState.value.bookList?.plus(bookResponse.docs)
                        ?: bookResponse.docs
                }
                Log.d("BookListViewModel", "download $newBookList")
                _bookListState.value = _bookListState.value.copy(
                    bookResponse = bookResponse,
                    errorStatus = ErrorStatus.NO_ERROR,
                    bookList = newBookList,
                    currentPosition = offset,
                    isLoadingPage = false
                )
            } else {
                if (isInitialLoad) {
                    _bookListState.value = _bookListState.value.copy(
                        errorStatus = ErrorStatus.NO_BOOKS_ERROR,
                    )
                }
                _bookListState.value = _bookListState.value.copy(
                    isLoadingPage = false
                )
            }
        } else {
            if (isInitialLoad) {
                _bookListState.value = _bookListState.value.copy(
                    errorStatus = ErrorStatus.NO_BOOKS_ERROR,
                )
            }
            _bookListState.value = _bookListState.value.copy(
                isLoadingPage = false
            )
        }
        Log.d("BookList", "ErroStatus ${_bookListState.value.errorStatus}")
    }

    private fun resetQuery() {
        _bookListState.value = _bookListState.value.copy(
            bookQuery = "",
            errorStatus = ErrorStatus.NO_ERROR,
            bookResponse = null,
            bookList = emptyList(),
            currentPosition = 0,
            isLoadingPage = false,
            isInitialScreen = true
        )
    }
}






















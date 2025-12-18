package com.example.bookfanapp.ui.view_models.book_details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.useCases.api.BookDetailsUseCase
import com.example.bookfanapp.domain.useCases.database.AddFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.ui.screens.book_details.BookDetailsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookDetailsViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val bookDetailsUseCase: BookDetailsUseCase,
    private val addFavouriteBookUseCase: AddFavouriteBookUseCase,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatusUseCase,
    private val deleteFavouriteBookUseCase: DeleteFavouriteBookUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<BookDetailsAction>(extraBufferCapacity = bufferSize)

    private val _bookDetailsState = MutableStateFlow(BookDetailsState())
    val bookDetailsState: StateFlow<BookDetailsState> = _bookDetailsState.asStateFlow()

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { bookDetailsAction ->
                when (bookDetailsAction) {
                    is BookDetailsAction.ChooseBook -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.FetchDetails -> {
                        launch {
                            fetchDetails(bookDetailsAction.bookItem, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.LoadSuccessDetails -> {
                        reduce(bookDetailsAction)
                        Log.d(
                            "BookDetailsViewModel",
                            "Description ${_bookDetailsState.value.bookItem?.bookDescription}}"
                        )
                    }

                    is BookDetailsAction.LoadErrorDetails -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.CheckFavouriteStatus -> {
                        launch {
                            checkFavouriteStatus(bookDetailsAction.keyBook, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.LoadSuccessFavouriteStatus -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorFavouriteStatus -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.ChangeFavouriteStatus -> {
                        launch {
                            changeFavouriteStatus(bookDetailsAction.bookItem, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.SuccessDeleted -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorDeleted -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.SuccessAdded -> {
                        reduce(bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorAdded -> {
                        reduce(bookDetailsAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        bookDetailsAction: BookDetailsAction
    ) {
        when (bookDetailsAction) {
            is BookDetailsAction.ChooseBook -> _bookDetailsState.update {
                it.copy(
                    bookItem = bookDetailsAction.bookItem
                )
            }

            is BookDetailsAction.FetchDetails -> _bookDetailsState.update {
                it.copy(
                    errorDetailsStatus = ErrorStatus.NO_ERROR,
                )
            }

            is BookDetailsAction.LoadSuccessDetails -> _bookDetailsState.update {
                it.copy(
                    bookItem = it.bookItem?.copy(
                        bookDescription = bookDetailsAction.bookDetails.description
                    ),
                    errorDetailsStatus = ErrorStatus.NO_ERROR
                )
            }

            is BookDetailsAction.LoadErrorDetails -> _bookDetailsState.update {
                it.copy(
                    errorDetailsStatus = bookDetailsAction.errorStatus
                )
            }

            is BookDetailsAction.CheckFavouriteStatus -> _bookDetailsState.update {
                it.copy(
                    checkFavouriteStatusError = null
                )
            }

            is BookDetailsAction.LoadSuccessFavouriteStatus -> _bookDetailsState.update {
                it.copy(
                    favouriteStatus = bookDetailsAction.status,
                    checkFavouriteStatusError = null
                )
            }

            is BookDetailsAction.LoadErrorFavouriteStatus -> _bookDetailsState.update {
                it.copy(
                    checkFavouriteStatusError = bookDetailsAction.error
                )
            }

            is BookDetailsAction.ChangeFavouriteStatus -> _bookDetailsState.update {
                it.copy(
                    changeFavouriteStatusError = null
                )
            }

            is BookDetailsAction.SuccessDeleted -> _bookDetailsState.update {
                it.copy(
                    favouriteStatus = false
                )
            }

            is BookDetailsAction.LoadErrorDeleted -> _bookDetailsState.update {
                it.copy(
                    changeFavouriteStatusError = bookDetailsAction.error
                )
            }

            is BookDetailsAction.SuccessAdded -> _bookDetailsState.update {
                it.copy(
                    favouriteStatus = true
                )
            }

            is BookDetailsAction.LoadErrorAdded -> _bookDetailsState.update {
                it.copy(
                    changeFavouriteStatusError = bookDetailsAction.error
                )
            }
        }
    }

    fun dispatch(bookDetailsAction: BookDetailsAction) {
        if (!actions.tryEmit(bookDetailsAction)) {
            error("Action buffer full!")
        }
    }

    private fun fetchDetails(
        bookItem: BookItem,
        bookDetailsAction: BookDetailsAction
    ) {
        reduce(bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {
            val content = bookDetailsUseCase(bookItem.keyBook)
            dispatch(bookDetailsAction = content)
        }
    }

    private fun checkFavouriteStatus(
        keyBook: String,
        bookDetailsAction: BookDetailsAction
    ) {
        reduce(bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {
            val result = checkFavouriteStatusUseCase(keyBook)
            dispatch(bookDetailsAction = result)
        }
    }


    private fun changeFavouriteStatus(
        bookItem: BookItem,
        bookDetailsAction: BookDetailsAction
    ) {
        reduce(bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {

            if (_bookDetailsState.value.favouriteStatus) {
                val result = deleteFavouriteBookUseCase(
                    bookItem.keyBook,
                    { BookDetailsAction.SuccessDeleted },
                    { e -> BookDetailsAction.LoadErrorDeleted(e) }
                )
                dispatch(bookDetailsAction = result)
            } else {
                val result = addFavouriteBookUseCase(
                    bookItem,
                    { BookDetailsAction.SuccessAdded },
                    { e -> BookDetailsAction.LoadErrorDeleted(e) }
                )
                dispatch(bookDetailsAction = result)
            }
        }
    }
}





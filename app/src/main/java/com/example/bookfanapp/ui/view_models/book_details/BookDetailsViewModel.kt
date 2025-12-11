package com.example.bookfanapp.ui.view_models.book_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var bookDetailsState by mutableStateOf(BookDetailsState())
        private set

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { bookDetailsAction ->
                when (bookDetailsAction) {
                    is BookDetailsAction.ChooseBook -> {
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.FetchDetails -> {
                        launch {
                            fetchDetails(bookDetailsAction.bookItem, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.LoadSuccessDetails -> {
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                        Log.d(
                            "BookDetailsViewModel",
                            "Description ${bookDetailsState.bookItem?.bookDescription}}"
                        )
                    }

                    is BookDetailsAction.LoadErrorDetails -> {
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.CheckFavouriteStatus -> {
                        launch {
                            checkFavouriteStatus(bookDetailsAction.keyBook, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.LoadSuccessFavouriteStatus -> {
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorFavouriteStatus -> {
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.ChangeFavouriteStatus -> {
                        launch {
                            changeFavouriteStatus(bookDetailsAction.bookItem, bookDetailsAction)
                        }
                    }

                    is BookDetailsAction.SuccessDeleted ->{
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorDeleted->{
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.SuccessAdded ->{
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }

                    is BookDetailsAction.LoadErrorAdded ->{
                        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        bookDetailsState: BookDetailsState,
        bookDetailsAction: BookDetailsAction
    ): BookDetailsState {
        return when (bookDetailsAction) {
            is BookDetailsAction.ChooseBook -> bookDetailsState.copy(
                bookItem = bookDetailsAction.bookItem
            )

            is BookDetailsAction.FetchDetails -> bookDetailsState.copy(
                errorDetailsStatus = ErrorStatus.NO_ERROR,
            )

            is BookDetailsAction.LoadSuccessDetails -> bookDetailsState.copy(
                bookItem = bookDetailsState.bookItem?.copy(
                    bookDescription = bookDetailsAction.bookDetails.description
                ),
                errorDetailsStatus = ErrorStatus.NO_ERROR
            )

            is BookDetailsAction.LoadErrorDetails -> bookDetailsState.copy(
                errorDetailsStatus = bookDetailsAction.errorStatus
            )

            is BookDetailsAction.CheckFavouriteStatus -> bookDetailsState.copy(
                checkFavouriteStatusError = null
            )

            is BookDetailsAction.LoadSuccessFavouriteStatus -> bookDetailsState.copy(
                favouriteStatus = bookDetailsAction.status,
                checkFavouriteStatusError = null
            )

            is BookDetailsAction.LoadErrorFavouriteStatus -> bookDetailsState.copy(
                checkFavouriteStatusError = bookDetailsAction.error
            )

            is BookDetailsAction.ChangeFavouriteStatus -> bookDetailsState.copy(
                changeFavouriteStatusError = null
            )

            is BookDetailsAction.SuccessDeleted -> bookDetailsState.copy(
                favouriteStatus = false
            )

            is BookDetailsAction.LoadErrorDeleted -> bookDetailsState.copy(
                changeFavouriteStatusError = bookDetailsAction.error
            )

            is BookDetailsAction.SuccessAdded-> bookDetailsState.copy(
                favouriteStatus = true
            )

            is BookDetailsAction.LoadErrorAdded -> bookDetailsState.copy(
                changeFavouriteStatusError = bookDetailsAction.error
            )
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
        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {
            val content = bookDetailsUseCase(bookItem.keyBook)
            dispatch(bookDetailsAction = content)
        }
    }

    private fun checkFavouriteStatus(
        keyBook: String,
        bookDetailsAction: BookDetailsAction
    ) {
        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {
            val result = checkFavouriteStatusUseCase(keyBook)
            dispatch(bookDetailsAction = result)
        }
    }


    private fun changeFavouriteStatus(
        bookItem: BookItem,
        bookDetailsAction: BookDetailsAction
    ) {
        bookDetailsState = reduce(bookDetailsState, bookDetailsAction)
        viewModelScope.launch(ioDispatcher) {

            if (bookDetailsState.favouriteStatus) {
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





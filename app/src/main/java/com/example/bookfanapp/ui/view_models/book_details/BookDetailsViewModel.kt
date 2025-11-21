package com.example.bookfanapp.ui.view_models.book_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.useCases.api.BookDetailsUseCase
import com.example.bookfanapp.domain.useCases.database.AddFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.GetFavouritesUseCase
import com.example.bookfanapp.ui.screens.book_details.BookDetailsState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

class BookDetailsViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val bookDetailsUseCase: BookDetailsUseCase,
    private val addFavouriteBookUseCase: AddFavouriteBookUseCase,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatusUseCase,
    private val deleteFavouriteBookUseCase: DeleteFavouriteBookUseCase,
    private val getFavouritesUseCase: GetFavouritesUseCase
) : ViewModel() {

    private val _bookDetailsState = MutableStateFlow(BookDetailsState())
    val bookDetailsState: StateFlow<BookDetailsState> = _bookDetailsState

    fun handleIntent(intent: BookDetailsIntent) {
        when (intent) {
            is BookDetailsIntent.LoadDetails -> loadAllDetails(intent.bookItem)
            is BookDetailsIntent.ChangeFavouriteStatus -> changeFavouriteStatus(intent.bookItem)
            is BookDetailsIntent.CheckFavouriteStatus -> checkFavouriteStatus(intent.keyBook)
        }
    }

    private fun loadAllDetails(bookItem: BookItem) {
        _bookDetailsState.value = _bookDetailsState.value.copy(
            bookItem = bookItem,
            error = null
        )

        viewModelScope.launch(ioDispatcher) {
            try {
                val content = bookDetailsUseCase(bookItem.keyBook)
                if (content != null) {
                    _bookDetailsState.value = _bookDetailsState.value.copy(
                        bookItem = _bookDetailsState.value.bookItem?.copy(
                            bookDescription = content.description
                        ),
                        error = null
                    )
                } else {
                    _bookDetailsState.value = _bookDetailsState.value.copy(
                        error = "something went wrong",
                    )
                }
            } catch (e: Exception) {
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    error = e.message,
                )
            }
        }
    }

    private fun checkFavouriteStatus(keyBook: String) {
        viewModelScope.launch {
            try {
                val isFavourite = withContext(ioDispatcher) {
                    checkFavouriteStatusUseCase(keyBook)
                }
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    favouriteStatus = isFavourite
                )
            } catch (e: IOException) {
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    error = "Network error: ${e.message}"
                )
            } catch (e: Exception) {
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    error = "Unknown error: ${e.message}"
                )
            }
        }
    }

    private fun changeFavouriteStatus(bookItem: BookItem) {
        viewModelScope.launch(ioDispatcher) {
            try {
                if (_bookDetailsState.value.favouriteStatus) {
                    deleteFavouriteBookUseCase(bookItem.keyBook)
                } else {
                    addFavouriteBookUseCase(bookItem)
                }
                val favouriteStatus = checkFavouriteStatusUseCase(bookItem.keyBook)
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    favouriteStatus = favouriteStatus,
                    error = null
                )
            } catch (e: Exception) {
                _bookDetailsState.value = _bookDetailsState.value.copy(
                    error = e.message,
                )
            }
        }
    }
}




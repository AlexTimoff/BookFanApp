package com.example.bookfanapp.ui.view_models.my_books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.GetFavouritesUseCase
import com.example.bookfanapp.ui.screens.my_books.MyBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatusUseCase,
    private val deleteFavouriteBookUseCase: DeleteFavouriteBookUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<MyBooksAction>(extraBufferCapacity = bufferSize)

    private val _myBooksState = MutableStateFlow(MyBooksState())
    val myBooksState: StateFlow<MyBooksState> = _myBooksState.asStateFlow()

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { myBooksAction ->
                when (myBooksAction) {
                    is MyBooksAction.ShowMyBooks -> {
                        launch {
                            showMyBooks(myBooksAction)
                        }
                    }

                    is MyBooksAction.LoadMyBooks -> {
                        reduce(myBooksAction)
                    }

                    is MyBooksAction.LoadError -> {
                        reduce(myBooksAction)
                    }

                    is MyBooksAction.DeleteMyBook -> {
                        launch {
                            deleteMyBook(myBooksAction.keyBook, myBooksAction)
                        }
                    }

                    is MyBooksAction.SuccessDeleted -> {
                        reduce(myBooksAction)
                        launch {
                            showMyBooks(myBooksAction)
                        }
                    }

                    is MyBooksAction.LoadErrorDeleted -> {
                        reduce(myBooksAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        myBooksAction: MyBooksAction
    ) {
        when (myBooksAction) {
            is MyBooksAction.ShowMyBooks -> _myBooksState.update {
                it.copy(
                    isLoading = true
                )
            }

            is MyBooksAction.LoadMyBooks -> _myBooksState.update {
                it.copy(
                    isLoading = false,
                    myBookList = myBooksAction.myBooks,
                    myBooksError = null
                )
            }

            is MyBooksAction.LoadError -> _myBooksState.update {
                it.copy(
                    isLoading = false,
                    myBooksError = myBooksAction.error
                )
            }

            is MyBooksAction.DeleteMyBook -> _myBooksState.update {
                it.copy(
                    deleteError = null
                )
            }

            is MyBooksAction.SuccessDeleted -> _myBooksState.update {
                it.copy(
                    deleteError = null
                )
            }

            is MyBooksAction.LoadErrorDeleted -> _myBooksState.update {
                it.copy(
                    deleteError = myBooksAction.error
                )
            }
        }
    }

    fun dispatch(myBooksAction: MyBooksAction) {
        if (!actions.tryEmit(myBooksAction)) {
            error("Action buffer full!")
        }
    }


    private fun showMyBooks(
        myBooksAction: MyBooksAction
    ) {
        reduce(myBooksAction)
        viewModelScope.launch(ioDispatcher) {
            val result = getFavouritesUseCase()
            dispatch(myBooksAction = result)
        }
    }

    private fun deleteMyBook(
        keyBook: String,
        myBooksAction: MyBooksAction
    ) {
        viewModelScope.launch(ioDispatcher) {
            reduce(myBooksAction)
            viewModelScope.launch(ioDispatcher) {
                val result = deleteFavouriteBookUseCase(
                    keyBook,
                    { MyBooksAction.SuccessDeleted },
                    { e -> MyBooksAction.LoadErrorDeleted(e) }
                )
                dispatch(myBooksAction = result)
            }
        }
    }
}
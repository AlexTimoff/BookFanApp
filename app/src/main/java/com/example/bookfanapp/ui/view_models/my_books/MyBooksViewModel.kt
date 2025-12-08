package com.example.bookfanapp.ui.view_models.my_books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.GetFavouritesUseCase
import com.example.bookfanapp.ui.screens.my_books.MyBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatusUseCase,
    private val deleteFavouriteBookUseCase: DeleteFavouriteBookUseCase,
) : ViewModel() {
    private val _myBooksState = MutableStateFlow(MyBooksState())
    val myBooksState: StateFlow<MyBooksState> = _myBooksState

    fun handleAction(action: MyBooksAction) {
        when (action) {
            is MyBooksAction.ShowMyBooks -> showMyBooks()
            is MyBooksAction.DeleteMyBook -> deleteMyBook(action.keyBook)
        }
    }

    private fun showMyBooks(){
        _myBooksState.value = _myBooksState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(ioDispatcher) {
            try {
                val myBooks = getFavouritesUseCase()
                _myBooksState.value = _myBooksState.value.copy(
                    isLoading = false,
                    myBookList = myBooks
                )
            } catch (e: Throwable) {
                _myBooksState.value = _myBooksState.value.copy(
                    isLoading = false,
                    error = e.message.toString()
                )
            }
        }
    }

    private fun deleteMyBook(keyBook:String){
        viewModelScope.launch(ioDispatcher) {
            try {
                val isBookInDB = checkFavouriteStatusUseCase(keyBook)
                if (isBookInDB) {
                    deleteFavouriteBookUseCase(keyBook)
                    val updatedBooks = getFavouritesUseCase()
                    _myBooksState.value = _myBooksState.value.copy(
                        myBookList = updatedBooks,
                        error = null
                    )
                } else {
                    val currentBooks = _myBooksState.value.myBookList
                    _myBooksState.value = _myBooksState.value.copy(
                        myBookList = currentBooks.filter { it.keyBook != keyBook },
                        error = null
                    )
                }
            } catch (e: Exception) {
                _myBooksState.value = _myBooksState.value.copy(
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
}
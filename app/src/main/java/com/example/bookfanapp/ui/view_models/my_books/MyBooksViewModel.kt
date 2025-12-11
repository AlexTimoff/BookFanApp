package com.example.bookfanapp.ui.view_models.my_books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.GetFavouritesUseCase
import com.example.bookfanapp.ui.screens.my_books.MyBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MyBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val getFavouritesUseCase: GetFavouritesUseCase,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatusUseCase,
    private val deleteFavouriteBookUseCase: DeleteFavouriteBookUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<MyBooksAction>(extraBufferCapacity = bufferSize)
    var myBooksState by mutableStateOf(MyBooksState())
        private set

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { myBooksAction ->
                when (myBooksAction) {
                    is MyBooksAction.ShowMyBooks -> {
                        launch{
                            showMyBooks(myBooksAction)
                        }
                    }

                    is MyBooksAction.LoadMyBooks -> {
                        myBooksState=reduce(myBooksState,myBooksAction)
                    }

                    is MyBooksAction.LoadError -> {
                        myBooksState=reduce(myBooksState,myBooksAction)
                    }

                    is MyBooksAction.DeleteMyBook -> {
                        launch {
                            deleteMyBook(myBooksAction.keyBook, myBooksAction)
                        }
                    }

                    is MyBooksAction.SuccessDeleted ->{
                        myBooksState=reduce(myBooksState,myBooksAction)
                        launch {
                            showMyBooks(myBooksAction)
                        }
                    }

                    is MyBooksAction.LoadErrorDeleted->{
                        myBooksState=reduce(myBooksState,myBooksAction)
                    }

                }
            }
        }
    }

    private fun reduce(
        myBooksState: MyBooksState,
        myBooksAction: MyBooksAction
    ): MyBooksState {
        return when (myBooksAction) {

            is MyBooksAction.ShowMyBooks -> myBooksState.copy(
                isLoading = true
            )

            is MyBooksAction.LoadMyBooks -> myBooksState.copy(
                isLoading = false,
                myBookList = myBooksAction.myBooks,
                myBooksError = null
            )

            is MyBooksAction.LoadError -> myBooksState.copy(
                isLoading = false,
                myBooksError=myBooksAction.error
            )

            is MyBooksAction.DeleteMyBook -> myBooksState.copy(
                deleteError = null
            )

            is MyBooksAction.SuccessDeleted -> myBooksState.copy(
                deleteError = null
            )

            is MyBooksAction.LoadErrorDeleted -> myBooksState.copy(
                deleteError = myBooksAction.error
            )

        }
    }

    fun dispatch(myBooksAction: MyBooksAction) {
        if (!actions.tryEmit(myBooksAction)) {
            error("Action buffer full!")
        }
    }


    private fun showMyBooks(
        myBooksAction: MyBooksAction
    ){
        myBooksState=reduce(myBooksState,myBooksAction)
        viewModelScope.launch(ioDispatcher) {
                val result = getFavouritesUseCase()
                dispatch(myBooksAction = result)
        }
    }

    private fun deleteMyBook(
        keyBook:String,
        myBooksAction: MyBooksAction
    ){
        viewModelScope.launch(ioDispatcher) {
            myBooksState = reduce(myBooksState, myBooksAction)
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
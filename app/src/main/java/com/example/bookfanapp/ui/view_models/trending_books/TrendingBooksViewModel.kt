package com.example.bookfanapp.ui.view_models.trending_books

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.domain.useCases.api.TrendingBooksListUseCase
import com.example.bookfanapp.ui.screens.trending_books.TrendingBooksState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

const val TRENDING_BOOKS =
    "trending_score_hourly_sum:[1 TO *] -subject:\"content_warning:cover\" language:rus -subject:\"content_warning:cover\" -subject:\"content_warning:cover\""

class TrendingBooksViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val trendingBooksListUseCase: TrendingBooksListUseCase,
) : ViewModel() {

    private val bufferSize = 64
    private val actions = MutableSharedFlow<TrendingBooksAction>(extraBufferCapacity = bufferSize)
    var trendingBooksState by mutableStateOf(TrendingBooksState())
        private set

    init {
        store()
    }

    private fun store() {
        viewModelScope.launch {
            actions.collect { trendingBooksAction ->
                when (trendingBooksAction) {
                    is TrendingBooksAction.FetchTrendingBooks -> {
                        launch{
                            fetchBooks( isInitialLoad = true, trendingBooksAction)
                        }
                    }

                    is TrendingBooksAction.FetchMoreTrendingBooks -> {
                        launch {
                            fetchBooks(isInitialLoad = false, trendingBooksAction)
                        }
                    }

                    is TrendingBooksAction.LoadSuccess -> {
                        trendingBooksState = reduce(trendingBooksState, trendingBooksAction)
                        Log.d("TrendingBooksViewModel", "booklist size ${trendingBooksState.bookList?.size}, booklist $trendingBooksState")
                    }

                    is TrendingBooksAction.LoadError -> {
                        trendingBooksState = reduce(trendingBooksState, trendingBooksAction)
                    }
                }
            }
        }
    }

    private fun reduce(
        trendingBooksState: TrendingBooksState,
        trendingBooksAction: TrendingBooksAction
    ): TrendingBooksState {
        return when (trendingBooksAction) {
            is TrendingBooksAction.FetchTrendingBooks -> trendingBooksState.copy(
                isLoadingPage = true,
                bookList = emptyList(),
                currentPosition = 0,
                errorStatus = ErrorStatus.NO_ERROR,
            )

            is TrendingBooksAction.FetchMoreTrendingBooks -> {
                trendingBooksState.copy(
                    isLoadingPage = false
                )
            }

            is TrendingBooksAction.LoadSuccess -> {
                val currentList = trendingBooksState.bookList ?: emptyList()
                val newList=currentList + trendingBooksAction.bookResponse.docs!!
                Log.d("TrendingBooksViewModel", "offset ${trendingBooksAction.offset.toString()}")
                trendingBooksState.copy(
                    isLoadingPage = false,
                    errorStatus = ErrorStatus.NO_ERROR,
                    bookList = newList,
                    currentPosition = trendingBooksAction.offset,
                )
            }

            is TrendingBooksAction.LoadError ->{
                if(!trendingBooksState.bookList.isNullOrEmpty()&&trendingBooksAction.errorStatus==ErrorStatus.NOTHING_FOUND){
                    trendingBooksState.copy(
                        isLoadingPage = false,
                        errorStatus = ErrorStatus.NO_ERROR
                    )
                }else{
                    trendingBooksState.copy(
                        isLoadingPage = false,
                        errorStatus = trendingBooksAction.errorStatus
                    )
                }
            }
        }
    }

    fun dispatch(trendingBooksAction: TrendingBooksAction) {
        if (!actions.tryEmit(trendingBooksAction)) {
            error("Action buffer full!")
        }
    }

    private fun fetchBooks( isInitialLoad: Boolean,  trendingBooksAction: TrendingBooksAction) {
        trendingBooksState = reduce(trendingBooksState, trendingBooksAction)
        val query= TRENDING_BOOKS
        val limit=12
        val offset = if (isInitialLoad) 0 else trendingBooksState.currentPosition + limit
        if (!isInitialLoad && trendingBooksState.isLoadingPage) return
        viewModelScope.launch(ioDispatcher) {
            val result = trendingBooksListUseCase(query, offset, limit)
            dispatch(trendingBooksAction = result)
            Log.d("TrendingBooksViewModel", "ErrorStatus ${result.toString()}")
        }
    }
}
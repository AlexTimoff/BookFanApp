package com.example.bookfanapp.ui.screens.search_books

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.ui.components.BookItemScreen
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksAction
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksViewModel
import com.example.bookfanapp.ui.view_models.shared.SharedBookAction
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.components.BookSearchField
import com.example.bookfanapp.ui.components.ErrorScreen
import com.example.bookfanapp.ui.components.InitialSearchScreen
import com.example.bookfanapp.ui.components.LoadingScreen
import com.example.bookfanapp.ui.components.Spacer
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchBooksScreen(
    viewModel: SearchBooksViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel(),
    navigateToDetails: () -> Unit
) {
    val state by viewModel.searchBooksState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (state.bookList.isNullOrEmpty()) {
            viewModel.handleAction(SearchBooksAction.Initial)
        }
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 80.dp)
    ) {

        Spacer(5.dp)

        BookSearchField(
            value = state.bookQuery,
            onValueChange = { bookQuery ->
                viewModel.handleAction(SearchBooksAction.UpdateBookQuery(bookQuery))
            },
            onSearch = {
                viewModel.handleAction(SearchBooksAction.LoadBookList(state.bookQuery, true))
            },
            placeholder = stringResource(R.string.search_input),
            modifier = Modifier
                .onFocusChanged { }
                .focusRequester(focusRequester),
            onReset = {
                viewModel.handleAction(SearchBooksAction.ResetQuery)
            }
        )

        Spacer(5.dp)

        when {
            state.isInitialScreen -> {
                InitialSearchScreen()
            }

            state.isLoadingPage -> {
                LoadingScreen()
            }

            else -> {
                when (state.errorStatus) {
                    ErrorStatus.NO_ERROR -> {
                        BookListContent(
                            state = state,
                            viewModel=viewModel,
                            onBookClick = { bookItem ->
                                sharedBookViewModel.handleAction(
                                    action = SharedBookAction.ChooseBook(bookItem)
                                )
                                navigateToDetails()
                            }
                        )
                    }

                    ErrorStatus.NETWORK_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_network))
                    }

                    ErrorStatus.SERVER_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_server))
                    }

                    ErrorStatus.NO_BOOKS_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_no_books))
                    }

                    ErrorStatus.UNKNOWN_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_unknown))
                    }
                }
            }
        }
    }
}

@Composable
private fun BookListContent(
    state: SearchBooksState,
    viewModel: SearchBooksViewModel,
    onBookClick: (BookItem) -> Unit
) {
    val books = state.bookList ?: return
    LazyColumn(
        state = rememberLazyListState().apply {
            LaunchedEffect(state.bookList) {
                snapshotFlow { layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .distinctUntilChanged()
                    .collect { lastVisibleItemIndex ->
                        val totalItems = state.bookList.size
                        val shouldLoadMore = lastVisibleItemIndex == totalItems - 1 && !state.isLoadingPage
                        if (shouldLoadMore) {
                            viewModel.handleAction(
                                SearchBooksAction.LoadBookList(
                                    name = state.bookQuery,
                                    isFirstLoad = false
                                )
                            )
                        }
                    }
            }
        },
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(
            items = books,
            key = { _, book -> book.keyBook }
        ) { _, bookItem ->
            BookItemScreen(
                onClick = { onBookClick(bookItem) },
                bookItem = bookItem,
                isButtonAdded = false,
                onFavouriteButtonClick = {}
            )
        }
    }
}





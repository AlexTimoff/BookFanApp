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
import androidx.navigation.NavHostController
import com.example.bookfanapp.R
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.ui.components.BookItem
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksIntent
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksViewModel
import com.example.bookfanapp.ui.view_models.shared.SharedBookIntent
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.components.BookSearchField
import com.example.bookfanapp.ui.components.ErrorScreen
import com.example.bookfanapp.ui.components.InitialSearchScreen
import com.example.bookfanapp.ui.components.LoadingScreen
import com.example.bookfanapp.ui.components.Spacer
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchBooksScreen(
    navController: NavHostController,
    viewModel: SearchBooksViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel()
) {
    val state by viewModel.searchBooksState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if(state.bookList.isNullOrEmpty()){
            viewModel.handleIntent(SearchBooksIntent.Initial)
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
                viewModel.handleIntent(SearchBooksIntent.UpdateBookQuery(bookQuery))
            },
            onSearch = {
                viewModel.handleIntent(SearchBooksIntent.LoadBookList(state.bookQuery, true))
            },
            placeholder = stringResource(R.string.search_input),
            modifier = Modifier
                .onFocusChanged { }
                .focusRequester(focusRequester),
            onReset = {
                viewModel.handleIntent(SearchBooksIntent.ResetQuery)
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

                    ErrorStatus.NO_ERROR -> {
                        LazyColumn(
                            content = {
                                val books = state.bookList
                                if (books != null) {
                                    itemsIndexed(books) { _, bookItem ->
                                        BookItem(
                                            onClick = {
                                                sharedBookViewModel.handleIntent(
                                                    SharedBookIntent.ChooseBook(
                                                        bookItem
                                                    )
                                                )
                                                navController.navigate("book_details_screen")
                                            },
                                            bookItem = bookItem,
                                            isButtonAdded = false,
                                            onFavouriteButtonClick = {}
                                        )
                                    }
                                }
                            },
                            contentPadding = PaddingValues(bottom = 100.dp),

                            state = rememberLazyListState().apply {
                                LaunchedEffect(state.bookList) {
                                    snapshotFlow { layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                                        .collect { lastVisibleItemIndex ->
                                            if ((lastVisibleItemIndex == (state.bookList?.size
                                                    ?: 0) - 1)
                                            ) {
                                                viewModel.handleIntent(
                                                    SearchBooksIntent.LoadBookList(
                                                        state.bookQuery, false
                                                    )
                                                )
                                            }
                                        }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}





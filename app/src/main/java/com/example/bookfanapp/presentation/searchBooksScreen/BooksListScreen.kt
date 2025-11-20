package com.example.bookfanapp.presentation.searchBooksScreen

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
import com.example.bookfanapp.presentation.sharedViewModel.SharedBookIntent
import com.example.bookfanapp.presentation.sharedViewModel.SharedBookViewModel
import com.example.bookfanapp.presentation.uiComponents.BookSearchField
import com.example.bookfanapp.presentation.uiComponents.ErrorScreen
import com.example.bookfanapp.presentation.uiComponents.InitialSearchScreen
import com.example.bookfanapp.presentation.uiComponents.LoadingScreen
import com.example.bookfanapp.presentation.uiComponents.Spacer
import org.koin.androidx.compose.koinViewModel


@Composable
fun BooksListScreen(
    navController: NavHostController,
    viewModel: BookListViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel()
) {
    val state by viewModel.bookListState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if(state.bookList.isNullOrEmpty()){
            viewModel.handleIntent(BookListIntent.Initial)
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
                viewModel.handleIntent(BookListIntent.UpdateBookQuery(bookQuery))
            },
            onSearch = {
                viewModel.handleIntent(BookListIntent.LoadBookList(state.bookQuery, true))
            },
            placeholder = stringResource(R.string.search_input),
            modifier = Modifier
                .onFocusChanged { }
                .focusRequester(focusRequester),
            onReset = {
                viewModel.handleIntent(BookListIntent.ResetQuery)
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
                                        BookItemScreen(
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
                                                    BookListIntent.LoadBookList(
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





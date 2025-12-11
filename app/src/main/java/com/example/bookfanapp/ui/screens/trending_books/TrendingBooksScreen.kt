package com.example.bookfanapp.ui.screens.trending_books

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksAction
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksViewModel
import com.example.bookfanapp.ui.components.ErrorScreen
import com.example.bookfanapp.ui.components.LinearProgressLoader
import com.example.bookfanapp.ui.components.Spacer
import com.example.bookfanapp.ui.components.ToSearchButton
import com.example.bookfanapp.ui.components.TrendingItemScreen
import com.example.bookfanapp.ui.theme.boldPurple_h5
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsViewModel
import kotlinx.coroutines.flow.distinctUntilChanged


@Composable
fun TrendingBooksScreen(
    viewModel: TrendingBooksViewModel,
    bookDetailsViewModel: BookDetailsViewModel,
    navigateToDetails: () -> Unit,
    navigateToSearch: () -> Unit
) {
    val state by viewModel::trendingBooksState
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (state.bookList.isNullOrEmpty()) {
            viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchTrendingBooks)
        }
    }

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                val totalItems = state.bookList?.size ?: 0
                Log.d("TrendingBooksList", " TotalItems:$totalItems")
                val shouldLoadMore = lastVisibleItemIndex == totalItems - 1 && !state.isLoadingPage
                Log.d("TrendingBooksList", "LastVisibleIndex: $lastVisibleItemIndex, TotalItems: $totalItems, isLoadingPage: ${state.isLoadingPage}")
                Log.d("TrendingBooksList", "Errorstatus: ${state.errorStatus}")
                if (shouldLoadMore) {
                    viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchMoreTrendingBooks)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 80.dp)
    ) {

        Spacer(5.dp)

        ToSearchButton( onClick = {navigateToSearch()})

        Spacer(20.dp)

        Text(
            text = stringResource(R.string.trends),
            style = MaterialTheme.typography.boldPurple_h5,
            modifier = Modifier
                .padding(top = 5.dp, start = 10.dp, end = 10.dp),
        )

        Spacer(5.dp)

        when {
            state.isLoadingPage -> {
                LinearProgressLoader()
            }

            else -> {
                when (state.errorStatus) {
                    ErrorStatus.NO_ERROR -> {
                        TrendingListContent(
                            state = state,
                            lazyGridState = lazyGridState,
                            onBookClick = { bookItem ->
                                bookDetailsViewModel.dispatch(BookDetailsAction.ChooseBook(bookItem))
                                navigateToDetails()
                            }
                        )
                    }

                    ErrorStatus.NETWORK_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_network),
                            isTryButtonAdded = true,
                            onClick = {viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchTrendingBooks)}
                        )
                    }

                    ErrorStatus.SERVER_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_server),
                            isTryButtonAdded = true,
                            onClick = {viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchTrendingBooks)}
                        )
                    }

                    ErrorStatus.NOTHING_FOUND -> {
                        ErrorScreen(stringResource(R.string.nothing_found),
                            isTryButtonAdded = true,
                            onClick = {viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchTrendingBooks)}
                        )
                    }

                    ErrorStatus.UNKNOWN_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_unknown),
                            isTryButtonAdded = true,
                            onClick = {viewModel.dispatch(trendingBooksAction = TrendingBooksAction.FetchTrendingBooks)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendingListContent(
    state: TrendingBooksState,
    lazyGridState: LazyGridState,
    onBookClick: (BookItem) -> Unit
) {
    val books = state.bookList ?: return
    LazyVerticalGrid(
        state = lazyGridState,
        contentPadding = PaddingValues(bottom = 100.dp),
        columns= GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
    ) {
        itemsIndexed(
            items = books,
            key = { _, book -> book.keyBook }
        ) { _, bookItem ->
            TrendingItemScreen(
                onClick = { onBookClick(bookItem) },
                bookItem = bookItem
            )
        }
    }
}
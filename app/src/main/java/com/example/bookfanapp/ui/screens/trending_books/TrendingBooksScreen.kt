package com.example.bookfanapp.ui.screens.trending_books

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.theme.Purple80
import com.example.bookfanapp.ui.theme.regularPurple_h7
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavHostController
import com.example.bookfanapp.domain.errors.ErrorStatus
import com.example.bookfanapp.ui.view_models.shared.SharedBookIntent
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.components.TrendingBookItem
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksIntent
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksViewModel
import com.example.bookfanapp.ui.components.ErrorScreen
import com.example.bookfanapp.ui.components.LinearProgressLoader
import com.example.bookfanapp.ui.components.Spacer
import com.example.bookfanapp.ui.theme.boldPurple_h5
import org.koin.androidx.compose.koinViewModel


@Composable
fun TrendingBooksScreen(
    navController: NavHostController,
    viewModel: TrendingBooksViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel()
) {
    val state by viewModel.trendingBooksState.collectAsState()

    LaunchedEffect(Unit) {
        if (state.bookList.isNullOrEmpty()) {
            viewModel.handleIntent(TrendingBooksIntent.LoadTrendingBooks(true))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 80.dp)
    ) {

        Spacer(5.dp)

        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 15.dp, end = 10.dp)
                .border(
                    width = 1.dp,
                    color = Purple80,
                    shape = RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    navController.navigate("book_search_screen")
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(R.string.search),
                tint = Purple80,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.regularPurple_h7,
            )
        }

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
                    ErrorStatus.NETWORK_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_network))
                    }

                    ErrorStatus.SERVER_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_server))
                    }

                    ErrorStatus.NO_BOOKS_ERROR -> {
                        ErrorScreen(stringResource(R.string.nothing_found))
                    }

                    ErrorStatus.UNKNOWN_ERROR -> {
                        ErrorScreen(stringResource(R.string.error_unknown))
                    }

                    ErrorStatus.NO_ERROR -> {

                        LazyVerticalGrid(
                            content = {
                                val books = state.bookList
                                if (books != null) {
                                    itemsIndexed(books) { _, bookItem ->
                                        TrendingBookItem(
                                            onClick = {
                                                sharedBookViewModel.handleIntent(
                                                    SharedBookIntent.ChooseBook(
                                                        bookItem
                                                    )
                                                )
                                                navController.navigate("book_details_screen")
                                            },
                                            bookItem = bookItem,
                                        )
                                    }
                                }

                            },
                            contentPadding = PaddingValues(bottom = 100.dp),

                            state = rememberLazyGridState().apply {
                                LaunchedEffect(state.bookList) {
                                    snapshotFlow { layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                                        .collect { lastVisibleItemIndex ->
                                            if ((lastVisibleItemIndex == (state.bookList?.size
                                                    ?: 0) - 1)
                                            ) {
                                                viewModel.handleIntent(
                                                    TrendingBooksIntent.LoadTrendingBooks(
                                                        false
                                                    )
                                                )
                                            }
                                        }
                                }
                            },
                            columns= GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
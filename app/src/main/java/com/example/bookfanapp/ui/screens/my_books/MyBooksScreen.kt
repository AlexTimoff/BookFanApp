package com.example.bookfanapp.ui.screens.my_books

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.ui.view_models.my_books.MyBooksAction
import com.example.bookfanapp.ui.view_models.my_books.MyBooksViewModel
import com.example.bookfanapp.ui.components.BookItemScreen
import com.example.bookfanapp.ui.view_models.shared.SharedBookAction
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.components.EmptyDatabaseScreen
import com.example.bookfanapp.ui.components.ErrorScreen
import com.example.bookfanapp.ui.components.LoadingScreen
import com.example.bookfanapp.ui.components.Spacer
import com.example.bookfanapp.ui.theme.boldPurple_h5
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyBooksScreen(
    viewModel: MyBooksViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel(),
    navigateToDetails: () -> Unit
) {
    val state by viewModel.myBooksState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleAction(MyBooksAction.ShowMyBooks)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 80.dp)
    ) {

        Spacer(5.dp)

        Text(
            text = stringResource(R.string.my_books),
            style = MaterialTheme.typography.boldPurple_h5,
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        )

        Spacer(5.dp)

        when {
            state.isLoading -> {
                LoadingScreen()
            }

            state.error != null -> {
                ErrorScreen(stringResource(R.string.database_error))
            }

            state.myBookList.isEmpty() -> {
                EmptyDatabaseScreen()
            }

            else -> {
                MyBooksListContent(
                    state=state,
                    onBookClick = {bookItem ->
                        sharedBookViewModel.handleAction(
                            action = SharedBookAction.ChooseBook(bookItem)
                        )
                        navigateToDetails()
                    },
                    onFavouriteButtonClick = {bookItem ->
                        viewModel.handleAction(MyBooksAction.DeleteMyBook(bookItem.keyBook))
                    }
                )
            }
        }
    }
}

@Composable
private fun MyBooksListContent(
    state: MyBooksState,
    onBookClick: (BookItem) -> Unit,
    onFavouriteButtonClick: (BookItem) -> Unit

) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        val books = state.myBookList
        itemsIndexed(books) { _, bookItem ->
            BookItemScreen(
                onClick = { onBookClick(bookItem) },
                bookItem = bookItem,
                isButtonAdded = true,
                favouriteStatus = true,
                onFavouriteButtonClick = {
                    onFavouriteButtonClick(bookItem)
                }
            )
        }
    }
}
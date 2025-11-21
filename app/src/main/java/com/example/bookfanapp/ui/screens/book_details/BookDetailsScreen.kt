package com.example.bookfanapp.ui.screens.book_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import com.example.bookfanapp.ui.components.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookfanapp.R
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.components.HorizontalBarChart
import com.example.bookfanapp.ui.components.ShowBlurredBackground
import com.example.bookfanapp.ui.components.ShowImage
import com.example.bookfanapp.ui.theme.regularBlack_h7
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.graphics.graphicsLayer
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsIntent
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsViewModel
import com.example.bookfanapp.ui.components.BookStarRating
import com.example.bookfanapp.ui.components.RawButtons
import com.example.bookfanapp.ui.theme.boldBlack_h5
import com.example.bookfanapp.ui.theme.boldBlack_h6
import com.example.bookfanapp.ui.theme.regularPurple_h7
import com.example.bookfanapp.ui.theme.semiboldGrey_h9

@Composable
fun BookDetailsScreen(
    navController: NavController,
    viewModel: BookDetailsViewModel = koinViewModel(),
    sharedBookViewModel: SharedBookViewModel = koinViewModel()
) {
    val state by viewModel.bookDetailsState.collectAsState()
    val chosenBook by sharedBookViewModel.chosenBook.collectAsState()

    LaunchedEffect(chosenBook) {
        chosenBook?.let { chosenBook ->
            viewModel.handleIntent(
                BookDetailsIntent.CheckFavouriteStatus(
                    keyBook = chosenBook.keyBook
                )
            )
            viewModel.handleIntent(
                BookDetailsIntent.LoadDetails(
                    bookItem = chosenBook
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp,bottom = 100.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(16.dp)

            BookImageInfo(state.bookItem)

            Column(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 15.dp,
                        end = 15.dp
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ShowTittleAndAuthors(state.bookItem)

                ShowCountRatingsInfo(state.bookItem)

                ShowBookDescription(state.bookItem)

                ShowLanguages(state.bookItem)
            }
        }

            RawButtons(
                onClickBack = {
                    navController.popBackStack()
                },
                onClickFavorite = {
                    viewModel.handleIntent(BookDetailsIntent.ChangeFavouriteStatus(bookItem = state.bookItem!!))
                },
                state = state
            )
    }
}

@Composable
fun BookImageInfo(bookItem: BookItem?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
    ) {

        ShowBlurredBackground(
            bookItem?.bookImageKey,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .graphicsLayer { this.alpha = 0.4f },
            ContentScale.FillWidth
        )

        ShowImage(
            bookItem?.bookImageKey,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 10.dp)
                .width(150.dp)
                .height(220.dp)
                .align(Alignment.BottomStart),
            ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .padding(end = 10.dp, bottom = 10.dp)
                .width(200.dp)
                .height(130.dp)
                .align(Alignment.BottomEnd),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Spacer(5.dp)

                bookItem?.bookRating?.let {
                    BookStarRating(bookItem.bookRating)
                }

                Spacer(5.dp)

                bookItem?.numberPages?.let {
                    Text(
                        text = stringResource(R.string.pages, bookItem.numberPages),
                        style = MaterialTheme.typography.semiboldGrey_h9,
                    )
                }

                Spacer(5.dp)

                bookItem?.firstPublishYear?.let {
                    Text(
                        text = stringResource(R.string.year, bookItem.firstPublishYear),
                        style = MaterialTheme.typography.semiboldGrey_h9,
                    )
                }
                Spacer(5.dp)

                bookItem?.wantReadCount?.let {
                    Text(
                        text = stringResource(
                            R.string.numbe_of_want_to_read,
                            bookItem.wantReadCount
                        ),
                        style = MaterialTheme.typography.semiboldGrey_h9,
                    )
                }

                Spacer(5.dp)

                bookItem?.alreadyReadCount?.let {
                    Text(
                        text = stringResource(
                            R.string.number_already_read,
                            bookItem.alreadyReadCount
                        ),
                        style = MaterialTheme.typography.semiboldGrey_h9,
                    )
                }
            }
        }
    }
}

@Composable
fun ShowTittleAndAuthors(bookItem: BookItem?) {
    bookItem?.authorNames?.let {
        Spacer(10.dp)
        Text(
            text = bookItem.authorNames.takeIf { it.isNotEmpty() }
                ?.joinToString(", ") ?: "",
            style = MaterialTheme.typography.boldBlack_h6,
            modifier = Modifier.fillMaxSize(),
        )
    }
    bookItem?.title?.let {
        Spacer(5.dp)
        Text(
            text = it,
            style = MaterialTheme.typography.boldBlack_h5,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun ShowCountRatingsInfo(bookItem: BookItem?) {
    bookItem?.ratingsCount?.let {
        val totalReviews = it
        val ratingsData = mapOf(
            1 to (bookItem.ratingScoreOne ?: 0),
            2 to (bookItem.ratingScoreTwo ?: 0),
            3 to (bookItem.ratingScoreThree ?: 0),
            4 to (bookItem.ratingScoreFour ?: 0),
            5 to (bookItem.ratingScoreFive ?: 0)
        )
        HorizontalBarChart(ratingsMap = ratingsData, totalReviews = totalReviews)
    }
}

@Composable
fun ShowBookDescription(bookItem: BookItem?) {
    Spacer(20.dp)
    Text(
        text = stringResource(R.string.about_book),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.boldBlack_h5,
    )
    Spacer(20.dp)
    Text(
        text = bookItem?.bookDescription ?: stringResource(R.string.no_description),
        style = MaterialTheme.typography.regularBlack_h7,
    )
}

@Composable
fun ShowLanguages(bookItem: BookItem?) {
    if (bookItem != null && !bookItem.languages.isNullOrEmpty()) {
        Spacer(20.dp)
        Text(
            text = stringResource(R.string.languages),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.boldBlack_h5,
        )
        Spacer(20.dp)
        FlowRow(
            horizontalArrangement = Arrangement.Start
        ) {
            bookItem.languages.forEach { language ->
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(30.dp)
                        .padding(3.dp)
                ) {
                    Text(
                        text = language.lowercase(),
                        style = MaterialTheme.typography.regularPurple_h7
                    )
                }
            }
        }
    }
}


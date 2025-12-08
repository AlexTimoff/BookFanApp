package com.example.bookfanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.roundToOneDecimal
import com.example.bookfanapp.ui.theme.Purple80
import com.example.bookfanapp.ui.theme.semiboldBlack_h7
import com.example.bookfanapp.ui.theme.regularGrey

@Composable
fun BookItemScreen(
    onClick: () -> Unit,
    bookItem: BookItem,
    modifier: Modifier = Modifier,
    isButtonAdded: Boolean,
    favouriteStatus: Boolean=false,
    onFavouriteButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 7.dp, bottom = 7.dp,
                start = 10.dp, end = 10.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = Purple80,
                shape = RoundedCornerShape(8.dp)
            )
            .height(175.dp)
            .background(color = White)
            .padding(15.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = modifier
                .width(90.dp)
                .height(160.dp)
        ) {
            ShowImage(bookItem.bookImageKey, modifier = Modifier.fillMaxSize(), ContentScale.Crop)
        }

        Spacer(modifier = modifier.width(20.dp))

        Column(
            modifier = modifier
                .weight(1f)
                .padding(end = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            bookItem.title?.let {
                Text(
                    text = it,
                    maxLines = 2,
                    style = MaterialTheme.typography.semiboldBlack_h7,
                )
            }

            bookItem.authorNames?.let {
                Spacer(5.dp)
                Text(
                    text = writeAuthor(bookItem.authorNames),
                    style = MaterialTheme.typography.regularGrey,
                )
            }

            bookItem.bookRating?.let {
                Spacer(5.dp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFFD700),
                        modifier = modifier.size(20.dp)
                    )
                    Spacer(modifier = modifier.width(4.dp))

                    Text(
                        text = bookItem.bookRating.roundToOneDecimal().toString(),
                        style = MaterialTheme.typography.regularGrey,
                    )

                    bookItem.ratingsCount?.let {
                        Spacer(modifier = modifier.width(4.dp))
                        Text(
                            text = "(${bookItem.ratingsCount} ratings)",
                            style = MaterialTheme.typography.regularGrey,
                        )
                    }
                }
            }

            bookItem.wantReadCount?.let {
                Spacer(5.dp)
                Text(
                    text = "${bookItem.wantReadCount} want to read",
                    style = MaterialTheme.typography.regularGrey,
                )
            }

        }

        if (isButtonAdded) {
            IconButton(onClick = { onFavouriteButtonClick.invoke() })
            {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = if (favouriteStatus) painterResource(R.drawable.ic_fill_heart) else painterResource(R.drawable.ic_favourite_empty),
                    contentDescription = "Like button",
                    tint = Purple80
                )
            }
        }
    }
}


private fun writeAuthor(authors: List<String>?): String {
    return authors?.firstOrNull() ?: ""
}




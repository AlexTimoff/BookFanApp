package com.example.bookfanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.screens.book_details.BookDetailsState
import com.example.bookfanapp.ui.theme.Purple40
import com.example.bookfanapp.ui.theme.Purple80


@Composable
fun RawButtons(state: BookDetailsState, onClickBack: () -> Unit, onClickFavorite: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { onClickBack() },
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back_btn),
                contentDescription = "Back button",
                tint = Purple80
            )
        }
        IconButton(
            onClick = { onClickFavorite() },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Purple40,
                            Purple40.copy(alpha = 0.6f),
                            Purple40.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        radius = 70f
                    )
                )
        ) {
            Icon(
                painter = if (state.favouriteStatus) painterResource(R.drawable.ic_fill_heart) else painterResource(
                    R.drawable.ic_favourite_empty
                ),
                contentDescription = "Favorite button",
                tint = Purple80
            )
        }
    }
}
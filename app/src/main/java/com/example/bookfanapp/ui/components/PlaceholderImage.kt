package com.example.bookfanapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.bookfanapp.R

@Composable
fun PlaceholderImage(modifier: Modifier = Modifier, contentScale: ContentScale) {
    Image(
        painter = painterResource(R.drawable.ic_no_book),
        contentDescription = "Нет обложки",
        contentScale = contentScale,
        modifier = modifier
    )
}
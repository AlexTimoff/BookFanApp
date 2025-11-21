package com.example.bookfanapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun ShowBlurredBackground(bookImageKey: String?,modifier: Modifier = Modifier, contentScale: ContentScale) {
    if (bookImageKey != null) {
        val imageLink = "https://covers.openlibrary.org/b/olid/${bookImageKey}-L.jpg"
        ImageBlurLoader(imageLink, modifier, contentScale)
    } else {
        PlaceholderImage(modifier, contentScale)
    }
}

@Composable
fun ImageBlurLoader(imageUrl: String, modifier: Modifier = Modifier, contentScale: ContentScale) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Image",
        contentScale = contentScale,
        modifier = modifier,
        error = {
            PlaceholderImage(modifier, contentScale)
        }
    )
}



package com.example.bookfanapp.presentation.uiComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest


@Composable
fun ShowImage(bookImageKey: String?, modifier: Modifier = Modifier, contentScale: ContentScale){
    if (bookImageKey != null) {
        val imageLink="https://covers.openlibrary.org/b/olid/${bookImageKey}-L.jpg"
        ImageLoader(imageLink, modifier, contentScale)
    } else {
        PlaceholderImage(modifier, contentScale)
    }
}

@Composable
fun ImageLoader(imageUrl: String, modifier: Modifier = Modifier, contentScale: ContentScale) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Image",
        contentScale = ContentScale.FillHeight,
        modifier = modifier,
        loading = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            PlaceholderImage(modifier, contentScale)
        }
    )
}
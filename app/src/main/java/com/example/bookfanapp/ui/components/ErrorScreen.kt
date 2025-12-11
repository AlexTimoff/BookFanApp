package com.example.bookfanapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bookfanapp.R

@Composable
fun ErrorScreen(
    message: String,
    isTryButtonAdded: Boolean,
    onClick:() -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.book_nothing_found))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        composition?.let {
            LottieAnimation(
                composition = it,
                progress = {progress},
                modifier = Modifier.size(150.dp)
            )
        }

        Spacer(20.dp)

        if(isTryButtonAdded){
            TryAgainButton( onClick=onClick)
        }

        Spacer(20.dp)

        Text(
            text = message,
            textAlign = TextAlign.Center,
        )
    }
}
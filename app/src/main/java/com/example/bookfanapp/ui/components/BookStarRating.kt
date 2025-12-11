package com.example.bookfanapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import com.example.bookfanapp.domain.roundToOneDecimal
import com.example.bookfanapp.ui.theme.boldGrey_h7

@Composable
fun BookStarRating(rating: Double, maxRating: Int = 5) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val fullStars = rating.toInt()
        val fractionalPart = rating - fullStars

        for (i in 1..maxRating) {
            val starType = when {
                i <= fullStars -> "full"
                i == fullStars + 1 -> {
                    when {
                        fractionalPart >= 0.75 -> "full"
                        fractionalPart >= 0.25 -> "half"
                        else -> "empty"
                    }
                }
                else -> "empty"
            }

            val drawableRes = when (starType) {
                "full" -> R.drawable.ic_fill_star
                "half" -> R.drawable.ic_half_star
                else -> R.drawable.ic_empty_star
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = "$starType Star",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = rating.roundToOneDecimal().toString(),
            style = MaterialTheme.typography.boldGrey_h7,
        )
    }
}
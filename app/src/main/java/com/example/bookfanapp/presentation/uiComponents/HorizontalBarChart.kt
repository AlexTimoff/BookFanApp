package com.example.bookfanapp.presentation.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.theme.boldBlack_h5


@Composable
fun HorizontalBarChart(ratingsMap: Map<Int, Int>, totalReviews: Int) {
    val maxCount = totalReviews.coerceAtLeast(1)
    val maxRating = ratingsMap.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$totalReviews \n ratings",
                style = MaterialTheme.typography.boldBlack_h5,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(0.5f),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                for (rating in maxRating downTo 1) {
                    val count = ratingsMap[rating] ?: 0
                    val ratio = count / maxCount.toFloat()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = stringResource(R.string.stars, rating), modifier = Modifier.width(40.dp))

                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .height(10.dp)
                                .background(Color.LightGray)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(ratio)
                                    .background(Color.Yellow)
                            )
                        }

                        Text(
                            text = "${ratingsMap[rating] ?: 0}",
                            modifier = Modifier
                                .weight(0.3f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
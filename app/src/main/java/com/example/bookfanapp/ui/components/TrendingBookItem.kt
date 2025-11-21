package com.example.bookfanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.roundToOneDecimal
import com.example.bookfanapp.ui.theme.Purple80
import com.example.bookfanapp.ui.theme.regularGrey

@Composable
fun TrendingBookItem(
    onClick: () -> Unit,
    bookItem: BookItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(180.dp)
            .clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier
            .fillMaxSize(),
            shape = RectangleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ShowImage(
                    bookItem.bookImageKey,
                    modifier = Modifier.fillMaxSize(),
                    ContentScale.Crop
                )
                bookItem.bookRating?.let { rating ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(5.dp)
                            .background(
                                color = Purple80.copy(alpha = 0.9f),
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = rating.roundToOneDecimal().toString(),
                                style = MaterialTheme.typography.regularGrey,
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.bookfanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.ui.theme.Purple40

@Composable
fun LinearProgressLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .width(150.dp)
                .height(4.dp)
                .background(color = Color.LightGray.copy(alpha = 0.3f))
                .clip(RoundedCornerShape(2.dp)),
            color = Purple40
        )
    }
}


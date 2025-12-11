package com.example.bookfanapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.theme.Purple80
import com.example.bookfanapp.ui.theme.regularPurple_h7


@Composable
fun TryAgainButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Purple80,
                shape = RoundedCornerShape(10.dp)
            )
            .height(56.dp)
            .width(160.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = onClick
    ) {
        Text(text = stringResource(R.string.try_again),
            style = MaterialTheme.typography.regularPurple_h7
        )
    }
}
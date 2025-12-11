package com.example.bookfanapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.theme.Purple80
import com.example.bookfanapp.ui.theme.regularPurple_h7

@Composable
fun ToSearchButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp, top = 15.dp, end = 10.dp)
            .border(
                width = 1.dp,
                color = Purple80,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = stringResource(R.string.search),
            tint = Purple80,
            modifier = Modifier.size(24.dp)
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.search),
            style = MaterialTheme.typography.regularPurple_h7,
        )
    }
}
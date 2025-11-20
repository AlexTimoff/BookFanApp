package com.example.bookfanapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bookfanapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)

val Typography.regularBlack_h7: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize7,
        fontFamily = FontFamily(Font(R.font.opensans_regular)),
        lineHeight = 22.sp,
        color = Black
    )
val Typography.regularPurple_h7: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize7,
        fontFamily = FontFamily(Font(R.font.opensans_regular)),
        lineHeight = 22.sp,
        color = Purple40
    )


val Typography.semiboldBlack_h7: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize7,
        fontFamily = FontFamily(Font(R.font.opensans_semibold)),
        lineHeight = 22.sp,
        color = Black
    )

val Typography.regularGrey: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize8,
        fontFamily = FontFamily(Font(R.font.opensans_regular)),
        lineHeight = 22.sp,
        color = DarkGray
    )


val Typography.semiboldGrey_h9: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize9,
        fontFamily = FontFamily(Font(R.font.opensans_semibold)),
        lineHeight = 22.sp,
        color = DarkGray
    )


val Typography.boldBlack_h5: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize5,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = Black
    )

val Typography.boldPurple_h5: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize5,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = Purple40
    )

val Typography.boldBlack_h6: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize6,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = Black
    )

val Typography.boldGrey_h7: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize7,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = DarkGray
    )

val Typography.boldGray_h5: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize5,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = DarkGray
    )

val Typography.boldBlack_h2: TextStyle
    get() = TextStyle(
        fontSize = Dimensions.TextSize2,
        fontFamily = FontFamily(Font(R.font.opensans_bold)),
        lineHeight = 22.sp,
        color = Black
    )
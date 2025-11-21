package com.example.bookfanapp.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.bookfanapp.ui.components.SetSystemBarsColor
import com.example.bookfanapp.ui.screens.home.HomeScreen
import com.example.bookfanapp.ui.theme.LightPink

@Composable
fun App() {
    val navController = rememberNavController()
    SetSystemBarsColor(LightPink.toArgb())
    HomeScreen(navController = navController)
}



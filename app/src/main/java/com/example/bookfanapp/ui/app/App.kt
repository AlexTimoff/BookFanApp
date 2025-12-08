package com.example.bookfanapp.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.bookfanapp.ui.components.SetSystemBarsColor
import com.example.bookfanapp.ui.navigation.BottomNavigationBar
import com.example.bookfanapp.ui.navigation.AppNavigation
import com.example.bookfanapp.ui.theme.LightPink

@Composable
fun App() {
    val navController = rememberNavController()
    SetSystemBarsColor(LightPink.toArgb())
    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding))
    }
}



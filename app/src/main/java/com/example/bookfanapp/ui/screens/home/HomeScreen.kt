package com.example.bookfanapp.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.bookfanapp.ui.navigation.AppNavigation
import com.example.bookfanapp.ui.components.navigation_bar.BottomNavigationBar


@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding))
    }
}
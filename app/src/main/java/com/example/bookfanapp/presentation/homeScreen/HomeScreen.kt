package com.example.bookfanapp.presentation.homeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookfanapp.presentation.navigation.AppNavigation
import com.example.bookfanapp.presentation.navigationBar.BottomNavigationBar


@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding))
    }
}
package com.example.bookfanapp.ui.components

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SetSystemBarsColor(
    color: Int
) {
    val view = LocalView.current
    val context = view.context
    val activity = context as? androidx.activity.ComponentActivity
    val window: Window? = activity?.window

    if (window != null) {
        window.statusBarColor = color
        window.navigationBarColor = color

        val insetsController = WindowCompat.getInsetsController(window, view)
        insetsController.apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }
}
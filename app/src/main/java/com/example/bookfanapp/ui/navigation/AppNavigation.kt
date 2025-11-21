package com.example.bookfanapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookfanapp.ui.screens.book_details.BookDetailsScreen
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsViewModel
import com.example.bookfanapp.ui.screens.home.HomeScreen
import com.example.bookfanapp.ui.screens.my_books.MyBooksScreen
import com.example.bookfanapp.ui.view_models.my_books.MyBooksViewModel
import com.example.bookfanapp.ui.view_models.search_books.SearchBooksViewModel
import com.example.bookfanapp.ui.screens.search_books.SearchBooksScreen
import com.example.bookfanapp.ui.view_models.shared.SharedBookViewModel
import com.example.bookfanapp.ui.screens.trending_books.TrendingBooksScreen
import com.example.bookfanapp.ui.view_models.trending_books.TrendingBooksViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val sharedBookViewModel: SharedBookViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = "trending_books_screen") {

        composable("trending_books_screen", enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            )
        },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                )
            }) {
            val subjectBooksViewModel: TrendingBooksViewModel = koinViewModel()
            TrendingBooksScreen(navController, subjectBooksViewModel,sharedBookViewModel)
        }

        composable(
            "book_search_screen", enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(300)
                )
            }) {
            val bookListViewModel: SearchBooksViewModel = koinViewModel()
            SearchBooksScreen(navController, bookListViewModel, sharedBookViewModel)
        }
        composable("my_books_screen") {
            val myBooksViewModel: MyBooksViewModel = koinViewModel()
            MyBooksScreen(navController, myBooksViewModel, sharedBookViewModel)
        }

        composable("book_details_screen") {
            val bookDetailsViewModel: BookDetailsViewModel = koinViewModel()
            BookDetailsScreen(navController, bookDetailsViewModel, sharedBookViewModel)
        }

        composable("home_screen") {
            HomeScreen(navController)
        }

    }
}
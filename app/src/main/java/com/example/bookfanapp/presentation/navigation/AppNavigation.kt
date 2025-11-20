package com.example.bookfanapp.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookfanapp.presentation.bookDetailsScreen.BookDetailsScreen
import com.example.bookfanapp.presentation.bookDetailsScreen.BookDetailsViewModel
import com.example.bookfanapp.presentation.homeScreen.HomeScreen
import com.example.bookfanapp.presentation.myBooksScreen.MyBooksScreen
import com.example.bookfanapp.presentation.myBooksScreen.MyBooksViewModel
import com.example.bookfanapp.presentation.searchBooksScreen.BookListViewModel
import com.example.bookfanapp.presentation.searchBooksScreen.BooksListScreen
import com.example.bookfanapp.presentation.sharedViewModel.SharedBookViewModel
import com.example.bookfanapp.presentation.trendingBooksScreen.SubjectBooksScreen
import com.example.bookfanapp.presentation.trendingBooksScreen.TrendingBooksViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val sharedBookViewModel: SharedBookViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = "subject_books_screen") {

        composable("subject_books_screen", enterTransition = {
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
            SubjectBooksScreen(navController, subjectBooksViewModel,sharedBookViewModel)
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
            val bookListViewModel: BookListViewModel = koinViewModel()
            BooksListScreen(navController, bookListViewModel, sharedBookViewModel)
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
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

    NavHost(
        navController = navController,
        startDestination = Route.TRENDING_BOOKS_SCREEN.name) {

        composable(
            route=Route.TRENDING_BOOKS_SCREEN.name,
            enterTransition = {
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
            val trendingBooksViewModel: TrendingBooksViewModel = koinViewModel()
            TrendingBooksScreen(
                viewModel = trendingBooksViewModel,
                sharedBookViewModel = sharedBookViewModel,
                navigateToDetails = {navController.navigate(route = Route.BOOK_DETAILS_SCREEN.name)},
                navigateToSearch = {navController.navigate(route=Route.SEARCH_BOOKS_SCREEN.name)}
                )
        }

        composable(
            route = Route.SEARCH_BOOKS_SCREEN.name,
            enterTransition = {
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
            val searchBooksViewModel: SearchBooksViewModel = koinViewModel()

            SearchBooksScreen(
                viewModel = searchBooksViewModel,
                sharedBookViewModel = sharedBookViewModel,
                navigateToDetails ={navController.navigate(route = Route.BOOK_DETAILS_SCREEN.name)}
            )
        }
        composable(route = Route.MY_BOOKS_SCREEN.name) {
            val myBooksViewModel: MyBooksViewModel = koinViewModel()
            MyBooksScreen(
                viewModel =  myBooksViewModel,
                sharedBookViewModel=sharedBookViewModel,
                navigateToDetails = {navController.navigate(route = Route.BOOK_DETAILS_SCREEN.name)}
                )
        }

        composable(Route.BOOK_DETAILS_SCREEN.name) {
            val bookDetailsViewModel: BookDetailsViewModel = koinViewModel()
            BookDetailsScreen(
                viewModel = bookDetailsViewModel,
                sharedBookViewModel=sharedBookViewModel,
                navigateOnBack = {navController.popBackStack()})
        }
    }
}
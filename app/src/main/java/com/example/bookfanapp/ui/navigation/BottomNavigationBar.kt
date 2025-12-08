package com.example.bookfanapp.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bookfanapp.R
import com.example.bookfanapp.ui.theme.LightPink
import com.example.bookfanapp.ui.theme.Purple40
import com.example.bookfanapp.ui.theme.Purple80

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val items = listOf(
        BarItem.Popular,
        BarItem.SearchBook,
        BarItem.MyBooks
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.height(100.dp),
        containerColor = LightPink,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painterResource(item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Top)
                    )
                },
                label = { Text(stringResource(item.destinationName)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple40,
                    unselectedIconColor = Purple80,
                    selectedTextColor = Purple40,
                    unselectedTextColor = Purple80,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }

}

sealed class BarItem(
    val route: String,
    val icon: Int,
    val destinationName: Int,
) {

    data object Popular: BarItem(
        route = Route.TRENDING_BOOKS_SCREEN.name,
        icon = R.drawable.ic_popular,
        destinationName = R.string.popular
    )

    data object SearchBook: BarItem(
        route = Route.SEARCH_BOOKS_SCREEN.name,
        icon = R.drawable.ic_search,
        destinationName = R.string.search
    )

    data object MyBooks: BarItem(
        route = Route.MY_BOOKS_SCREEN.name,
        icon = R.drawable.ic_my_books,
        destinationName = R.string.my_books
    )
}
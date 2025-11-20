package com.example.bookfanapp.presentation.navigationBar

import com.example.bookfanapp.R

sealed class BarItem(
    val route: String,
    val icon: Int,
    val destinationName: Int,
) {

    data object Popular: BarItem(
        route = "subject_books_screen",
        icon = R.drawable.ic_popular,
        destinationName = R.string.popular
    )

    data object SearchBook: BarItem(
        route = "book_search_screen",
        icon = R.drawable.ic_search,
        destinationName = R.string.search
    )

    data object MyBooks: BarItem(
        route = "my_books_screen",
        icon = R.drawable.ic_my_books,
        destinationName = R.string.my_books
    )
}


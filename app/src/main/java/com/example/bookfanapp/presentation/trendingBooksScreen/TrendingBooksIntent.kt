package com.example.bookfanapp.presentation.trendingBooksScreen

sealed class TrendingBooksIntent {
    data class LoadTrendingBooks(val isInitialLoad: Boolean) : TrendingBooksIntent()
}
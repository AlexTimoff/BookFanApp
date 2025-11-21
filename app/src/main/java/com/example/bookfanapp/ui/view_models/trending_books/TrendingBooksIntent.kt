package com.example.bookfanapp.ui.view_models.trending_books

sealed class TrendingBooksIntent {
    data class LoadTrendingBooks(val isInitialLoad: Boolean) : TrendingBooksIntent()
}
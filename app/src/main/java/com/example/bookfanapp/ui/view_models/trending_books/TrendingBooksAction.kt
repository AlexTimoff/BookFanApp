package com.example.bookfanapp.ui.view_models.trending_books

sealed class TrendingBooksAction {
    data class LoadTrendingBooks(val isInitialLoad: Boolean) : TrendingBooksAction()
}
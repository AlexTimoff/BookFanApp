package com.example.bookfanapp.ui.screens.search_books

import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.errors.ErrorStatus

data class SearchBooksState (
  val bookQuery: String="",
  val errorStatus: ErrorStatus = ErrorStatus.NO_ERROR,
  val bookList: List<BookItem>? = emptyList(),
  val currentPosition: Int = 0,
  val isLoadingPage: Boolean=false,
  val isInitialScreen: Boolean=false,
)
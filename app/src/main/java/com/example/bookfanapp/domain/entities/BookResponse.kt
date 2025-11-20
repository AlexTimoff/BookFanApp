package com.example.bookfanapp.domain.entities

data class BookResponse (
    val query: String,
    val docs: List<BookItem>?
)
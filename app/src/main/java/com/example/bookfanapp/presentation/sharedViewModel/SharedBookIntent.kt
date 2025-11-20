package com.example.bookfanapp.presentation.sharedViewModel

import com.example.bookfanapp.domain.entities.BookItem

sealed class SharedBookIntent {
    data class ChooseBook(val bookItem: BookItem) : SharedBookIntent()
}

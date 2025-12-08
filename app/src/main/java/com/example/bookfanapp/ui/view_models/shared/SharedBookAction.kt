package com.example.bookfanapp.ui.view_models.shared

import com.example.bookfanapp.domain.entities.BookItem

sealed class SharedBookAction {
    data class ChooseBook(val bookItem: BookItem) : SharedBookAction()
}

package com.example.bookfanapp.ui.view_models.shared

import androidx.lifecycle.ViewModel
import com.example.bookfanapp.domain.entities.BookItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedBookViewModel: ViewModel() {
    private val _chosenBook = MutableStateFlow<BookItem?>(null)
    val chosenBook: StateFlow<BookItem?> = _chosenBook.asStateFlow()

    fun handleIntent(intent: SharedBookIntent) {
        when (intent) {
            is SharedBookIntent.ChooseBook -> {
                _chosenBook.value = intent.bookItem
            }
        }
    }
}
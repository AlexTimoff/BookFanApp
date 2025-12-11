package com.example.bookfanapp.data.api_open_library.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    val q: String,
    val docs: List<BookItemDto>
)

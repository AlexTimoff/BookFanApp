package com.example.bookfanapp.data.apiOpenLibrary.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class BookResponseDto(
    val q: String,
    val docs: List<BookItemDto>
)

package com.example.bookfanapp.data.api_open_library.network.entities


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BookDetailsDto(
    val description: JsonElement? = null
)

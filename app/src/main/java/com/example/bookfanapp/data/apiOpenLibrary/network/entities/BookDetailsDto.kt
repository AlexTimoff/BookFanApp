package com.example.bookfanapp.data.apiOpenLibrary.network.entities


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class BookDetailsDto(
    val description: JsonElement? = null
)

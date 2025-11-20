package com.example.bookfanapp.domain

import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookDetailsDto
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundToOneDecimal(): Double {
    return BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toDouble()
}

fun getDescriptionText(dto: BookDetailsDto): String? {
    val element = dto.description ?: return null
    return when {
        element is JsonPrimitive && element.isString -> element.content
        element is JsonObject -> {
            element["value"]?.jsonPrimitive?.content
        }
        else -> null
    }
}




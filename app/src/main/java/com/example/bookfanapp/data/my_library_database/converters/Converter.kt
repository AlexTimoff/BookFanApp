package com.example.bookfanapp.data.my_library_database.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converter {
    @TypeConverter
    fun fromList(list: List<String>) : String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun fromStr(text: String) : List<String> {
        return Json.decodeFromString(text)
    }
}
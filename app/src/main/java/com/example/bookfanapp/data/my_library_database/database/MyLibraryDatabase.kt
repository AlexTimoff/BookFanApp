package com.example.bookfanapp.data.my_library_database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookfanapp.data.my_library_database.converters.Converter
import com.example.bookfanapp.data.my_library_database.dao.BookDao
import com.example.bookfanapp.data.my_library_database.entity.BookEntityDto

@Database(
    entities = [BookEntityDto::class], version = 1
)
@TypeConverters(
    Converter::class
)
abstract class MyLibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
package com.example.bookfanapp.data.myLibraryDatabase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookfanapp.data.myLibraryDatabase.converters.Converter
import com.example.bookfanapp.data.myLibraryDatabase.dao.BookDao
import com.example.bookfanapp.data.myLibraryDatabase.entity.BookEntityDto

@Database(
    entities = [BookEntityDto::class], version = 1
)
@TypeConverters(
    Converter::class
)
abstract class MyLibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
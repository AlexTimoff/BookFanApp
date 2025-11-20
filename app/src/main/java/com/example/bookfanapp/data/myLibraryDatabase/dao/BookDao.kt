package com.example.bookfanapp.data.myLibraryDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookfanapp.data.myLibraryDatabase.entity.BookEntityDto

@Dao
interface BookDao {
    @Query("SELECT * FROM bookList")
    suspend fun getAllBooksFromDB(): List<BookEntityDto>

    @Insert(entity = BookEntityDto::class)
    suspend fun insert(bookItemDto: BookEntityDto)

    @Query("SELECT * FROM bookList WHERE keyBook = :keyBook")
    suspend fun getBookFromDB(keyBook: String): BookEntityDto?

    @Query("DELETE FROM bookList WHERE keyBook = :keyBook")
    suspend fun deleteBookFromDB(keyBook: String)
}
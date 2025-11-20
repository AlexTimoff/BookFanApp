package com.example.bookfanapp.data.myLibraryDatabase.repositories

import com.example.bookfanapp.data.myLibraryDatabase.dao.BookDao
import com.example.bookfanapp.data.myLibraryDatabase.mappers.FromBookEntityDtoToBookItemMapper
import com.example.bookfanapp.data.myLibraryDatabase.mappers.ToBookEntityDtoMapper
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.repositories.DatabaseRepository

class DatabaseRepositoryImpl(
    private val bookDao: BookDao,
    private val fromBookEntityDtoToBookItemMapper: FromBookEntityDtoToBookItemMapper,
    private val toBookEntityDtoMapper: ToBookEntityDtoMapper
) : DatabaseRepository {
    override suspend fun getAllFavourites(): List<BookItem> {
        val bookEntities = bookDao.getAllBooksFromDB()
        return bookEntities.map { entity -> fromBookEntityDtoToBookItemMapper(entity) }
    }

    override suspend fun addFavouriteBook(bookItem: BookItem) {
        bookDao.insert(toBookEntityDtoMapper(bookItem))
    }

    override suspend fun deleteFavouriteBook(keyBook: String) {
        bookDao.deleteBookFromDB(keyBook)
    }

    override suspend fun checkFavouriteStatus(keyBook: String): Boolean {
        val response = bookDao.getBookFromDB(keyBook)
        return if (response == null) {
            false
        } else {
            true
        }
    }
}
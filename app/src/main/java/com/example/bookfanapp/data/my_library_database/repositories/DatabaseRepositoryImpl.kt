package com.example.bookfanapp.data.my_library_database.repositories

import com.example.bookfanapp.data.my_library_database.dao.BookDao
import com.example.bookfanapp.data.my_library_database.mappers.FromBookEntityDtoToBookItemMapper
import com.example.bookfanapp.data.my_library_database.mappers.ToBookEntityDtoMapper
import com.example.bookfanapp.domain.entities.BookItem
import com.example.bookfanapp.domain.repositories.DatabaseRepository
import com.example.bookfanapp.ui.view_models.book_details.BookDetailsAction
import com.example.bookfanapp.ui.view_models.my_books.MyBooksAction

class DatabaseRepositoryImpl(
    private val bookDao: BookDao,
    private val fromBookEntityDtoToBookItemMapper: FromBookEntityDtoToBookItemMapper,
    private val toBookEntityDtoMapper: ToBookEntityDtoMapper
) : DatabaseRepository {

    override suspend fun getAllFavourites(): MyBooksAction {
        return try {
            val bookEntities = bookDao.getAllBooksFromDB()
            val result=bookEntities.map { entity -> fromBookEntityDtoToBookItemMapper(entity) }
            MyBooksAction.LoadMyBooks(result)
        } catch (e: Throwable) {
            MyBooksAction.LoadError(e.toString())
        }
    }

    override suspend fun <T> addFavouriteBook(
        bookItem: BookItem,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T {
        return try {
            bookDao.insert(toBookEntityDtoMapper(bookItem))
            createSuccessAction()
        } catch (e: Throwable) {
            createErrorAction(e.toString())
        }
    }

    override suspend fun <T> deleteFavouriteBook(
        keyBook: String,
        createSuccessAction: () -> T,
        createErrorAction: (String) -> T
    ): T {
        return try {
            bookDao.deleteBookFromDB(keyBook)
            createSuccessAction()
        } catch (e: Throwable) {
            createErrorAction(e.toString())
        }
    }

    override suspend fun checkFavouriteStatus(keyBook: String): BookDetailsAction {
        return try {
            val response = bookDao.getBookFromDB(keyBook)
            BookDetailsAction.LoadSuccessFavouriteStatus(response != null)
        } catch (e: Throwable) {
            BookDetailsAction.LoadErrorFavouriteStatus(e.toString())
        }
    }
}
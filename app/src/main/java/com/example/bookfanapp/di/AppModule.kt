package com.example.bookfanapp.di


import androidx.room.Room
import com.example.bookfanapp.data.my_library_database.dao.BookDao
import com.example.bookfanapp.data.my_library_database.database.MyLibraryDatabase
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookDetailsMapper
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookItemMapper
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookListMapper
import com.example.bookfanapp.data.my_library_database.mappers.FromBookEntityDtoToBookItemMapper
import com.example.bookfanapp.data.my_library_database.mappers.ToBookEntityDtoMapper
import com.example.bookfanapp.domain.useCases.api.BookDetailsUseCase
import com.example.bookfanapp.domain.useCases.api.SearchBooksUseCase
import com.example.bookfanapp.domain.useCases.api.TrendingBooksListUseCase
import com.example.bookfanapp.domain.useCases.database.AddFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.CheckFavouriteStatusUseCase
import com.example.bookfanapp.domain.useCases.database.DeleteFavouriteBookUseCase
import com.example.bookfanapp.domain.useCases.database.GetFavouritesUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    //Database
    single<MyLibraryDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MyLibraryDatabase::class.java,
            "book_database"
        ).build()
    }

    single<BookDao> { get<MyLibraryDatabase>().bookDao() }

    //UseCases
    single { SearchBooksUseCase(get()) }
    single { TrendingBooksListUseCase(get()) }
    single { BookDetailsUseCase(get()) }
    single { AddFavouriteBookUseCase(get()) }
    single { CheckFavouriteStatusUseCase(get()) }
    single { DeleteFavouriteBookUseCase(get()) }
    single { GetFavouritesUseCase(get()) }

    //Mappers
    single { DtoToBookListMapper(get()) }
    single { DtoToBookItemMapper() }
    single { DtoToBookDetailsMapper() }
    single { FromBookEntityDtoToBookItemMapper() }
    single { ToBookEntityDtoMapper() }
}
package com.example.bookfanapp.di

import com.example.bookfanapp.data.apiOpenLibrary.network.mapper.DtoToBookDetailsMapper
import com.example.bookfanapp.data.apiOpenLibrary.network.mapper.DtoToBookListMapper
import com.example.bookfanapp.data.apiOpenLibrary.repositories.BookRepositoryImpl
import com.example.bookfanapp.data.myLibraryDatabase.mappers.FromBookEntityDtoToBookItemMapper
import com.example.bookfanapp.data.myLibraryDatabase.mappers.ToBookEntityDtoMapper
import com.example.bookfanapp.data.myLibraryDatabase.repositories.DatabaseRepositoryImpl
import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.domain.repositories.DatabaseRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

object RepositoryModule {
    val repositoryModule = module{
        single<BookRepository> {
            BookRepositoryImpl(
                get(named("IoDispatcher")),
                get(),
                get<DtoToBookListMapper>(),
                get<DtoToBookDetailsMapper>()
            )
        }

        single<DatabaseRepository> {
            DatabaseRepositoryImpl(
                get(),
                get<FromBookEntityDtoToBookItemMapper>(),
                get<ToBookEntityDtoMapper>()
            )
        }

    }
}
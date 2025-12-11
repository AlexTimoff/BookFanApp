package com.example.bookfanapp.di

import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookDetailsMapper
import com.example.bookfanapp.data.api_open_library.network.mapper.DtoToBookListMapper
import com.example.bookfanapp.data.api_open_library.repositories.BookRepositoryImpl
import com.example.bookfanapp.data.my_library_database.mappers.FromBookEntityDtoToBookItemMapper
import com.example.bookfanapp.data.my_library_database.mappers.ToBookEntityDtoMapper
import com.example.bookfanapp.data.my_library_database.repositories.DatabaseRepositoryImpl
import com.example.bookfanapp.domain.repositories.BookRepository
import com.example.bookfanapp.domain.repositories.DatabaseRepository
import org.koin.dsl.module

object RepositoryModule {
    val repositoryModule = module{
        single<BookRepository> {
            BookRepositoryImpl(
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
package com.example.bookfanapp.di

import com.example.bookfanapp.data.api_open_library.network.BookApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object NetworkModule {
    val networkModule = module {
        single { BookApiService(get()) }
        single {
            HttpClient(CIO) {
                install(Logging) {
                    level = LogLevel.INFO
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, "application/json")
                }
            }
        }
    }
}

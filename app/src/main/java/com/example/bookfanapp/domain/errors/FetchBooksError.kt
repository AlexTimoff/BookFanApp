package com.example.bookfanapp.domain.errors

sealed class FetchBooksError : Throwable() {
    class NetworkError(message: String?) : FetchBooksError()
    class ServerError(statusCode: String) : FetchBooksError()
    class UnknownError(message: String?) : FetchBooksError()
}
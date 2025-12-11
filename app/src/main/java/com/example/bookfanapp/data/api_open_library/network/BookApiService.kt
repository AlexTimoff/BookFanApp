package com.example.bookfanapp.data.api_open_library.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

const val BASE_URL = "https://openlibrary.org"

class BookApiService(
    private val httpClient: HttpClient
) {
    suspend fun getBooksList(query: String, offset: Int, limit: Int): HttpResponse {
        val url = "$BASE_URL/search.json"
        val request = httpClient.get(
            url
        ) {
            parameter("q", query)
            parameter("offset", offset)
            parameter("limit", limit)
            parameter(
                "fields",
                "author_key,author_name,cover_edition_key,edition_count,first_publish_year,key,language," +
                        "number_of_pages_median,title,person,ratings_average,ratings_count,ratings_count_1," +
                        "ratings_count_2,ratings_count_3,ratings_count_4,ratings_count_5,want_to_read_count," +
                        "currently_reading_count,already_read_count"
            )
        }
        return request
    }

    suspend fun getTrendingBooks(query: String, offset: Int, limit: Int): HttpResponse {
        val url = "$BASE_URL/search.json"
        val request = httpClient.get(
            url
        ) {
            parameter("q", query)
            parameter("sort", "trending")
            parameter("offset", offset)
            parameter("limit", limit)
            parameter(
                "fields",
                "author_key,author_name,cover_edition_key,edition_count,first_publish_year,key,language," +
                        "number_of_pages_median,title,person,ratings_average,ratings_count,ratings_count_1," +
                        "ratings_count_2,ratings_count_3,ratings_count_4,ratings_count_5,want_to_read_count," +
                        "currently_reading_count,already_read_count"
            )
        }
        return request
    }

    suspend fun getBookDetailsInfo(key: String): HttpResponse {
        val request = "$BASE_URL${key}.json"
        return httpClient.get(request)
    }
}
package com.example.bookfanapp.data.apiOpenLibrary.network.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookItemDto (
    @SerialName("author_key")
    val authorKey: List<String>?=null,
    @SerialName("author_name")
    val authorName: List<String>?=null,
    @SerialName("cover_edition_key")
    val coverEditionKey: String?=null,
    @SerialName("edition_count")
    val editionCount: Int?=null,
    @SerialName("first_publish_year")
    val firstPublishYear: Int?=null,
    @SerialName("key")
    val keyBook: String,
    @SerialName("language")
    val languages: List<String>?=null,
    @SerialName("number_of_pages_median")
    val numberPages: Int? = null,
    @SerialName("title")
    val title: String,
    @SerialName("person")
    val persons: List<String>?=null,
    @SerialName("ratings_average")
    val bookRating: Double? = null,
    @SerialName("ratings_count")
    val ratingsCount: Int? = null,
    @SerialName("ratings_count_1")
    val ratingScoreOne: Int? = null,
    @SerialName("ratings_count_2")
    val ratingScoreTwo: Int? = null,
    @SerialName("ratings_count_3")
    val ratingScoreThree: Int? = null,
    @SerialName("ratings_count_4")
    val ratingScoreFour: Int? = null,
    @SerialName("ratings_count_5")
    val ratingScoreFive: Int? = null,
    @SerialName("want_to_read_count")
    val wantReadCount: Int? = null,
    @SerialName("currently_reading_count")
    val currentlyReadingCount: Int? = null,
    @SerialName("already_read_count")
    val alreadyReadCount: Int? = null,
)



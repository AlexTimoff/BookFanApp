package com.example.bookfanapp.data.my_library_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookList")
data class BookEntityDto (
    @PrimaryKey
    @ColumnInfo(name = "keyBook")
    val keyBook: String,

    @ColumnInfo(name = "authorKeys")
    val authorKeys: List<String>?,

    @ColumnInfo(name = "authorNames")
    val authorNames: List<String>?,

    @ColumnInfo(name = "bookImageKey")
    val bookImageKey: String?,

    @ColumnInfo(name = "editionCount")
    val editionCount: Int?,

    @ColumnInfo(name = "firstPublishYear")
    val firstPublishYear: Int?,

    @ColumnInfo(name = "languages")
    val languages: List<String>?,

    @ColumnInfo(name = "numberPages")
    val numberPages: Int?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "bookDescription")
    val bookDescription: String?,

    @ColumnInfo(name = "persons")
    val persons: List<String>?,

    @ColumnInfo(name = "bookRating")
    val bookRating: Double?,

    @ColumnInfo(name = "ratingsCount")
    val ratingsCount: Int?,

    @ColumnInfo(name = "ratingScoreOne")
    val ratingScoreOne: Int?,

    @ColumnInfo(name = "ratingScoreTwo")
    val ratingScoreTwo: Int?,

    @ColumnInfo(name = "ratingScoreThree")
    val ratingScoreThree: Int?,

    @ColumnInfo(name = "ratingScoreFour")
    val ratingScoreFour: Int?,

    @ColumnInfo(name = "ratingScoreFive")
    val ratingScoreFive: Int?,

    @ColumnInfo(name = "wantReadCount")
    val wantReadCount: Int?,

    @ColumnInfo(name = "currentlyReadingCount")
    val currentlyReadingCount: Int?,

    @ColumnInfo(name = "alreadyReadCount")
    val alreadyReadCount: Int?
)
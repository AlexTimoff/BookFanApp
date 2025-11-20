package com.example.bookfanapp.data.apiOpenLibrary.network.mapper

import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookItemDto
import com.example.bookfanapp.domain.entities.BookItem

class DtoToBookItemMapper {
    operator fun invoke(bookItemDto: BookItemDto): BookItem {
        return with(bookItemDto){
            BookItem(
                keyBook=keyBook,
                authorKeys=authorKey?: emptyList(),
                authorNames = authorName?: emptyList(),
                bookImageKey = coverEditionKey,
                editionCount = editionCount?:0,
                firstPublishYear=firstPublishYear,
                languages=languages?: emptyList(),
                numberPages=numberPages,
                title=title,
                bookDescription = null,
                persons=persons?: emptyList(),
                bookRating=bookRating,
                ratingsCount=ratingsCount,
                ratingScoreOne=ratingScoreOne,
                ratingScoreTwo=ratingScoreTwo,
                ratingScoreThree=ratingScoreThree,
                ratingScoreFour=ratingScoreFour,
                ratingScoreFive=ratingScoreFive,
                wantReadCount=wantReadCount,
                currentlyReadingCount=currentlyReadingCount,
                alreadyReadCount=alreadyReadCount
            )
        }
    }
}
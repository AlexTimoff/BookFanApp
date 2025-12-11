package com.example.bookfanapp.data.my_library_database.mappers

import com.example.bookfanapp.data.my_library_database.entity.BookEntityDto
import com.example.bookfanapp.domain.entities.BookItem

class ToBookEntityDtoMapper {
    operator fun invoke(bookItem: BookItem): BookEntityDto {
        return with(bookItem){
            BookEntityDto(
                keyBook=keyBook,
                authorKeys=authorKeys,
                authorNames = authorNames,
                bookImageKey = bookImageKey,
                editionCount = editionCount,
                firstPublishYear=firstPublishYear,
                languages=languages,
                numberPages=numberPages,
                title=title,
                bookDescription = bookDescription,
                persons=persons,
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
package com.example.bookfanapp.data.myLibraryDatabase.mappers

import com.example.bookfanapp.data.myLibraryDatabase.entity.BookEntityDto
import com.example.bookfanapp.domain.entities.BookItem

class FromBookEntityDtoToBookItemMapper {
    operator fun invoke(bookEntityDto: BookEntityDto): BookItem {
        return with(bookEntityDto){
            BookItem(
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
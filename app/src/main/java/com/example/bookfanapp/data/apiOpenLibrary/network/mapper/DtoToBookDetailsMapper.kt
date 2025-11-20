package com.example.bookfanapp.data.apiOpenLibrary.network.mapper

import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookDetailsDto
import com.example.bookfanapp.domain.entities.BookDetails
import com.example.bookfanapp.domain.getDescriptionText

class DtoToBookDetailsMapper{
    operator fun invoke(bookDetailsDto: BookDetailsDto): BookDetails {
        return with(bookDetailsDto) {
            BookDetails(
                description = getDescriptionText(bookDetailsDto),
            )
        }
    }
}


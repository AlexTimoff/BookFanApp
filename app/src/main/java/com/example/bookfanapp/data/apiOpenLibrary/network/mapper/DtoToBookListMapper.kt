package com.example.bookfanapp.data.apiOpenLibrary.network.mapper

import com.example.bookfanapp.data.apiOpenLibrary.network.entities.BookResponseDto
import com.example.bookfanapp.domain.entities.BookResponse

class DtoToBookListMapper(
    private val dtoToBookItemMapper: DtoToBookItemMapper
) {
    operator fun invoke(bookListDto: BookResponseDto): BookResponse {
        return with(bookListDto) {
            BookResponse(
                query = q,
                docs = docs.map { dtoToBookItemMapper(it) }?: emptyList(),
            )
        }
    }
}
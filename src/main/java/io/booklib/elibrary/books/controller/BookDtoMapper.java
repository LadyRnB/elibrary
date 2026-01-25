package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;

import java.util.UUID;

public class BookDtoMapper {

    public static BookDTO mapRequestToDto(CreationBookRequest bookRequest){
        return new BookDTO(null, bookRequest.title(), bookRequest.author(), bookRequest.genre(), bookRequest.isbn());
    }

    public static BookResponse mapDtoToResponse(BookDTO bookDTO){
        return new BookResponse(bookDTO.id(), bookDTO.title(), bookDTO.author(), bookDTO.genre(), bookDTO.isbn());
    }
    //A mapper to transform a request into a DTO with ID, to be used in @PutMapping
    public static BookDTO mapRequestAndIdToDTO(CreationBookRequest bookRequest, UUID bookId) {
        return new BookDTO(bookId, bookRequest.title(), bookRequest.author(), bookRequest.genre(), bookRequest.isbn());
    }
}

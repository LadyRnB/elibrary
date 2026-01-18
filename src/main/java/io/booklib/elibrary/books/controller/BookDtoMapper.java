package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;

public class BookDtoMapper {

    public static BookDTO map(CreationBookRequest bookRequest){
        return new BookDTO(null, bookRequest.title(), bookRequest.author(), bookRequest.genre(), bookRequest.isbn());
    }

    public static BookResponse map(BookDTO bookDTO){
        return new BookResponse(bookDTO.id(), bookDTO.title(), bookDTO.author(), bookDTO.genre(), bookDTO.isbn());
    }
}

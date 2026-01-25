package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;

public class BookEntityMapper {

    public static BookEntity mapDtoToEntity(BookDTO bookDTO){
        return new BookEntity(bookDTO.id(), bookDTO.title(), bookDTO.author(), bookDTO.genre(), bookDTO.isbn());
    }

    public static BookDTO mapEntityToDto(BookEntity bookEntity){
        return new BookDTO(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getGenre(), bookEntity.getIsbn());
    }
}

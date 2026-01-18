package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;

public class BookEntityMapper {

    public static BookEntity map(BookDTO bookDTO){
        return new BookEntity(bookDTO.id(), bookDTO.title(), bookDTO.author(), bookDTO.genre(), bookDTO.isbn());
    }

    public static BookDTO map(BookEntity bookEntity){
        return new BookDTO(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getGenre(), bookEntity.getIsbn());
    }
}

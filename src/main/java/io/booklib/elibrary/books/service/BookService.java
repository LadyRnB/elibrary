package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;
import io.booklib.elibrary.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookDTO createBook(BookDTO newBookDTO) {
        // the method argument is a Book, but we need a BookEntity. Then we create a BookEntity from the Book
        BookEntity bookEntity = new BookEntity(null, newBookDTO.title(), newBookDTO.author(), newBookDTO.genre(), newBookDTO.isbn());
        // we save the BookEntity. The save Method will return another BookEntity as saved in the Database. We will reuse the bookEntity to store the new result
        bookEntity = bookRepository.save(bookEntity);
        // since the method returns a Book to the controller, then we need to convert the BookEntity got from the repository to a Book
        return new BookDTO(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getGenre(), bookEntity.getIsbn());
    }

}

package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;
import io.booklib.elibrary.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private final BookEntityMapper mapper = new BookEntityMapper();

    public BookDTO createBook(BookDTO newBookDTO) {
        BookEntity bookEntity = bookRepository.save(mapper.map(newBookDTO));
        return mapper.map(bookEntity);
    }

}

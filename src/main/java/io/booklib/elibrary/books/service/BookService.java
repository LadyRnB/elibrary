package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;
import io.booklib.elibrary.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.booklib.elibrary.books.service.BookEntityMapper.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookDTO createBook(BookDTO newBookDTO) {
        BookEntity bookEntity = bookRepository.save(mapDtoToEntity(newBookDTO));
        return mapEntityToDto(bookEntity);
    }

    public Optional<BookDTO> findBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(bookEntity -> mapEntityToDto(bookEntity));
    }

    public void deleteBookById(UUID bookId) {
        bookRepository.deleteById(bookId);
    }

    public Optional<BookDTO> updateBook(BookDTO bookDTO) {
        if (bookRepository.existsById(bookDTO.id())){
            BookEntity bookEntity = mapDtoToEntity(bookDTO);
            return Optional.ofNullable(mapEntityToDto(bookRepository.save(bookEntity)));
        }
        else {
            return Optional.empty();
        }

    }

    public Optional<BookDTO> partiallyUpdate(BookDTO bookDTO) {
        return bookRepository.findById(bookDTO.id())
                .map(bookEntity -> this.fillEntityWithDto(bookDTO, bookEntity))
                .map(bookEntity -> bookRepository.save(bookEntity))
                .map(bookEntity -> mapEntityToDto(bookEntity));
    }

    private BookEntity fillEntityWithDto(BookDTO bookDTO, BookEntity bookEntity) {
        if (Objects.nonNull(bookDTO.id())) {
            bookEntity.setId(bookDTO.id());
        }
        if (bookDTO.title() != null) {
            bookEntity.setTitle(bookDTO.title());
        }
        if (Objects.nonNull(bookDTO.author())) {
            bookEntity.setAuthor(bookDTO.author());
        }
        if (bookDTO.genre() != null) {
            bookEntity.setGenre(bookDTO.genre());
        }
        if (Objects.nonNull(bookDTO.isbn())) {
            bookEntity.setIsbn(bookDTO.isbn());
        }
        return bookEntity;
    }
}

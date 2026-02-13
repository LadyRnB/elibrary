package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;
import io.booklib.elibrary.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.booklib.elibrary.books.service.BookEntityMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

//   Whether we use the annotation @RequiredArgsConstructor to automatically create a constructor (injection)
//   or:
//   public BookService (BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }

    public BookDTO createBook(BookDTO newBookDTO) {
        BookEntity bookEntity = bookRepository.save(mapDtoToEntity(newBookDTO));
        log.debug("A new book {} is created", bookEntity);
        return mapEntityToDto(bookEntity);
    }

    public Optional<BookDTO> findBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .map(bookEntity -> mapEntityToDto(bookEntity));
    }

    public List<BookDTO> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(bookEntity -> mapEntityToDto(bookEntity))
                .toList();
    }
//     or other ways:
//    1- getAllBooks with "for-loop":
//    public List<BookDTO> getAllBooks(){
//        List<BookEntity> listBookEntity = bookRepository.findAll();
//        List<BookDTO> listBookDTO = new ArrayList<>();
//        for(BookEntity bookEntity : listBookEntity){
//            BookDTO bookDTO = mapEntityToDto(bookEntity);
//            listBookDTO.add(bookDTO);
//        }
//        return listBookDTO;
//    }
//    2- getAllBooks with "forEach":
//    public List<BookDTO> getAllBooks(){
//        List<BookDTO> listBookDTO = new ArrayList<>();
//        bookRepository.findAll().forEach(bookEntity -> listBookDTO.add(mapEntityToDto(bookEntity)));
//        return listBookDTO;
//    }

    public void deleteBookById(UUID bookId) {
        bookRepository.deleteById(bookId);
        log.debug("The book with ID: {} is deleted", bookId);
    }

    public Optional<BookDTO> updateBook(BookDTO bookDTO) {
        if (bookRepository.existsById(bookDTO.id())){
            BookEntity bookEntity = bookRepository.save(mapDtoToEntity(bookDTO));
            log.debug("The book with ID: {} is updated", bookEntity.getId());
            return Optional.of(mapEntityToDto(bookEntity));
        }
        else {
            log.debug("The book with ID: {} is not found", bookDTO.id());
            return Optional.empty();
        }

    }

    public Optional<BookDTO> partiallyUpdate(BookDTO bookDTO) {
        Optional<BookDTO> optionalBookDTO = bookRepository.findById(bookDTO.id())
                .map(bookEntity -> this.fillEntityWithDto(bookDTO, bookEntity))
                .map(bookEntity -> bookRepository.save(bookEntity))
                .map(bookEntity -> mapEntityToDto(bookEntity));
        if (optionalBookDTO.isPresent()) {
            log.debug("The book with ID: {} is partially updated", optionalBookDTO.get().id());
        }
        else {
            log.debug("The book with ID: {} is not found", bookDTO.id());
        }
        return  optionalBookDTO;
    }

    private BookEntity fillEntityWithDto(BookDTO bookDTO, BookEntity bookEntity) {
        if (Objects.nonNull(bookDTO.id())) {
            bookEntity.setId(bookDTO.id());
            log.trace("The book.id is updated with {}", bookDTO.id());
        }
        if (bookDTO.title() != null) {
            bookEntity.setTitle(bookDTO.title());
            log.trace("The book.title is updated with {}", bookDTO.title());
        }
        if (Objects.nonNull(bookDTO.author())) {
            bookEntity.setAuthor(bookDTO.author());
            log.trace("The book.author is updated with {}", bookDTO.author());
        }
        if (bookDTO.genre() != null) {
            bookEntity.setGenre(bookDTO.genre());
            log.trace("The book.genre is updated with {}", bookDTO.genre());
        }
        if (Objects.nonNull(bookDTO.isbn())) {
            bookEntity.setIsbn(bookDTO.isbn());
            log.trace("The book.isbn is updated with {}", bookDTO.isbn());
        }
        return bookEntity;
    }
}

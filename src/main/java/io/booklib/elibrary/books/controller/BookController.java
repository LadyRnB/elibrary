package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static io.booklib.elibrary.books.controller.BookDtoMapper.*;

@RestController
@RequestMapping("books")
@Slf4j
public class BookController {

    // To get a logger for this class, we add an annotation @Slf4j or simply:
    // private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @PostMapping
    public BookResponse createBook(@RequestBody CreationBookRequest bookRequest) {
        log.info("Creating a new book {}", bookRequest);
        BookDTO bookDTO = BookDtoMapper.mapRequestToDto(bookRequest);
        return mapDtoToResponse(bookService.createBook(bookDTO));
    }

    @GetMapping("{bookId}")
    public BookResponse getBookById(@PathVariable UUID bookId){
        return bookService.findBookById(bookId)
                .map(bookDto -> mapDtoToResponse(bookDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }
//    or other way:
//    @GetMapping("{bookId}")
//    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID bookId){
//        return bookService.findBookById(bookId)
//                .map(bookDTO -> map(bookDTO))
//                .map(bookResponse -> ResponseEntity.ok(bookResponse))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping
    public void getAllBooks() {}

    @DeleteMapping("{bookId}")
    public void deleteBookById(@PathVariable UUID bookId){
        log.info("deleting the book {}", bookId);
        bookService.deleteBookById(bookId);
    }

    @PutMapping("{bookId}")
    public BookResponse updateBook(@PathVariable UUID bookId,@RequestBody CreationBookRequest bookRequest){
        BookDTO bookDTO = mapRequestAndIdToDTO(bookRequest, bookId);
        return mapDtoToResponse(bookService.updateBook(bookDTO)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist")));
    }

    @PatchMapping("{bookId}")
    public BookResponse partiallyUpdate(@PathVariable UUID bookId, @RequestBody CreationBookRequest bookRequest){
        return bookService.partiallyUpdate(mapRequestAndIdToDTO(bookRequest, bookId))
                .map(bookDTO -> mapDtoToResponse(bookDTO))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist"));
    }


}

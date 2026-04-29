package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import io.booklib.elibrary.common.exceptions.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static io.booklib.elibrary.books.controller.BookDtoMapper.*;

@RestController
@RequestMapping("books")
@Slf4j
public class BookController {

    // To get a logger for this class, we add an annotation @Slf4j or simply:
    // private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
//    or an annotation: @RequiredArgsConstructor would suffice

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    public BookResponse createBook(@RequestBody @Valid CreationBookRequest bookRequest) {
        log.info("Request received to create a new book: {}", bookRequest);
        BookDTO bookDTO = bookService.createBook(BookDtoMapper.mapRequestToDto(bookRequest));
        return mapDtoToResponse(bookDTO);
    }

    @GetMapping("{bookId}")
    public BookResponse getBookById(@PathVariable UUID bookId){
        log.info("Request received to fetch a book with ID: {}", bookId);
        BookDTO bookDto = bookService.findBookById(bookId);
        return mapDtoToResponse(bookDto);
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
     public List<BookResponse> getAllBooks() {
        log.info("Request to get all books is received");
        return bookService.getAllBooks()
                .stream()
                .map(bookDTO -> mapDtoToResponse(bookDTO))
                .toList();
    }

    @DeleteMapping("{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public void deleteBookById(@PathVariable UUID bookId){
        log.info("Request received to delete a book with ID: {}", bookId);
        bookService.deleteBookById(bookId);
    }

    @PutMapping("{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    public BookResponse updateBook(@PathVariable UUID bookId,@RequestBody CreationBookRequest bookRequest){
        log.info("Request received to update a book with ID: {}", bookId);
        BookDTO bookDTO = mapRequestAndIdToDTO(bookRequest, bookId);
        return mapDtoToResponse(bookService.updateBook(bookDTO)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist")));
    }

    @PatchMapping("{bookId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    public BookResponse partiallyUpdate(@PathVariable UUID bookId, @RequestBody CreationBookRequest bookRequest){
        log.info("Request received to update partially a book with ID: {}", bookId);
        return bookService.partiallyUpdate(mapRequestAndIdToDTO(bookRequest, bookId))
                .map(bookDTO -> mapDtoToResponse(bookDTO))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}

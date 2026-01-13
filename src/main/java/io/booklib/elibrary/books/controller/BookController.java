package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    private final BookDtoMapper mapper = new BookDtoMapper();

    @PostMapping
    public BookResponse createBook(@RequestBody CreationBookRequest bookRequest){
        System.out.println("The new book is: " + bookRequest.title());
        BookDTO bookDTO = bookService.createBook(mapper.map(bookRequest));
        return mapper.map(bookDTO);
    }
}

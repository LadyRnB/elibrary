package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.booklib.elibrary.books.controller.BookDtoMapper.map; // Import a library of static method in order to use "map" without calling the class like(BookDtoMapper.map)

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public BookResponse createBook(@RequestBody CreationBookRequest bookRequest){
        System.out.println("The new book is: " + bookRequest.title());
        BookDTO bookDTO = bookService.createBook(map(bookRequest));
        return map(bookDTO);
    }
}

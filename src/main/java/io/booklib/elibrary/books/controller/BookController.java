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

    @PostMapping
    public BookResponse createBook(@RequestBody CreationBookRequest bookRequest){
        System.out.println("The new book is: " + bookRequest.title());
       BookDTO respondedBookDTO = bookService.createBook(new BookDTO(null, bookRequest.title(), bookRequest.author(), bookRequest.genre(), bookRequest.isbn()));
        return new BookResponse(respondedBookDTO.id(), respondedBookDTO.title(), respondedBookDTO.author(), respondedBookDTO.genre(), respondedBookDTO.isbn());
    }
}

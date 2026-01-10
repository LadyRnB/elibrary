package io.booklib.elibrary.books.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    @PostMapping
    public CreationBookResponse createBook(@RequestBody CreationBookRequest newBook){
        System.out.println("The new book is: " + newBook.title());
        UUID bookId = UUID.randomUUID();
        return new CreationBookResponse(bookId); // or we can remove the previous line: UUID bookId, and write it directly: return new CreationBookResponse(UUID.randomUUID());

    }
}

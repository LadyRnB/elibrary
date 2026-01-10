package io.booklib.elibrary.books.controller;

import lombok.Data;

@Data
public class CreationBookRequest {
    private final String title;
    private final String author;
    private final String genre;
    private final String isbn;
}

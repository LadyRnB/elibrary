package io.booklib.elibrary.books.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class CreationBookResponse {
    private final UUID bookId;
}

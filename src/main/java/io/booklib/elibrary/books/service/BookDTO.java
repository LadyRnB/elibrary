package io.booklib.elibrary.books.service;

import java.util.UUID;

public record BookDTO(UUID id, String title, String author, String genre, String isbn) {}

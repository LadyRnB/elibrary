package io.booklib.elibrary.books.controller;

import java.util.UUID;

public record BookResponse(UUID bookId, String title, String author, String genre, String isbn) {}

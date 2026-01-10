package io.booklib.elibrary.books.controller;

public record CreationBookRequest(String title, String author, String genre, String isbn) {}

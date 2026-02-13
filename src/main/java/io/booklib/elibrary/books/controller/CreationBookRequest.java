package io.booklib.elibrary.books.controller;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreationBookRequest(
        @NotNull @Size(max = 20)
        String title,

        @NotNull
        String author,

        @NotNull
        String genre,

        @NotNull @Pattern(
                regexp = "^(?:[0-9]-?){9}[0-9]X]$|^(?:[0-9]-?){13}$",
                message = "Invalid ISBN format")
        String isbn
) {}

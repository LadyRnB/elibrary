package io.booklib.elibrary.users.controller;

import jakarta.validation.constraints.*;

public record UserRegistrationRequest(
        @NotNull @Size(max = 20, min = 5) @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9.-_]{2,15}$",
                message = "Invalid username format")
        String username,

        @NotBlank @Size(min = 5)
        String password,

        @NotNull @Email(
                regexp = "^[a-z][a-z0-9._+-]{1,30}@[a-z]{3,10}\\.[a-z]{2,5}",
                message = " Invalid Email format"
        )
        String email
){}

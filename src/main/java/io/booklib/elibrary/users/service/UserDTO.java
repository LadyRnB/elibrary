package io.booklib.elibrary.users.service;

import java.util.UUID;

public record UserDTO (UUID userId, String username, String password, String email) {}


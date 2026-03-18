package io.booklib.elibrary.users.controller;

import io.booklib.elibrary.users.service.UserRole;

import java.util.UUID;

public record UserResponse (UUID userId, String username, String email, UserRole role) {}

package com.bookstore.dto;

import com.bookstore.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDtos {

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank @Email String email,
            @NotBlank String password,
            @NotNull Role role
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record AuthResponse(String token) {
    }
}

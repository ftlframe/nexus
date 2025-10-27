package com.plague.nexus.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for handling login requests.
 * Uses a Java Record for immutability and conciseness.
 */
public record LoginRequest(
        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Password is required.")
        String password
) {}
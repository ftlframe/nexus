package com.plague.nexus.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

// This is a "record", a modern Java feature for simple data classes.
// Spring Boot knows how to parse JSON into this object.
public record RegisterRequest(
        @NotBlank
        @Size(min = 3, max = 20)
        String username,

        @NotBlank
        @Size(max = 50)
        @Email
        String email,

        @NotBlank
        @Size(min = 6, max = 40)
        String password,

        // A set of strings: e.g., ["ADMIN", "WAREHOUSE"]
        Set<String> roles
) {}
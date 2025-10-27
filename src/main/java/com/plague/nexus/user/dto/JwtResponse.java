package com.plague.nexus.user.dto;

import com.plague.nexus.user.User;
import java.util.List;

/**
 * Data Transfer Object (DTO) for the successful login response.
 */
public record JwtResponse(
        String token,
        String type,
        Long id,
        String username,
        String email
) {
    // A convenient constructor to map our User entity/details to the response fields
    public JwtResponse(String token, User userDetails) {
        this(
                token,
                "Bearer", // Standard token type prefix
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        );
    }
}
package com.plague.nexus.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // We'll need this one for logging in.
    Optional<User> findByUsername(String username);

    // We'll need these for registration to prevent duplicates.
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
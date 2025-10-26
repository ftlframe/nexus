// File: src/main/java/com/plague/nexus/user/UserRepository.java

package com.plague.nexus.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // --- Spring Data JPA's "Query Method" Magic ---
    // We just declare a method with a specific name, and Spring
    // automatically writes the SQL query for us.

    /**
     * Finds a User by their username, but only within a specific Organization.
     * This is critical for our multi-tenant security.
     *
     * @param username       The username to search for.
     * @param organizationId The ID of the organization to search within.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByUsernameAndOrganizationId(String username, Long organizationId);

    /**
     * Finds a User by their email, but only within a specific Organization.
     *
     * @param email          The email to search for.
     * @param organizationId The ID of the organization to search within.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByEmailAndOrganizationId(String email, Long organizationId);

    /**
     * Finds a User by their globally unique email address.
     * This will be our main method for authentication.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with this email already exists within a specific Organization.
     * This is faster than findByEmail... because it can stop searching
     * as soon as it finds a single match.
     *
     * @param email          The email to check.
     * @param organizationId The ID of the organization to check.
     * @return true if a user with this email exists in this org, false otherwise.
     */
    boolean existsByEmailAndOrganizationId(String email, Long organizationId);

    boolean existsByEmail(String email);
}
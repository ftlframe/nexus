// File: src/main/java/com/plague/nexus/user/User.java

package com.plague.nexus.user;

import com.plague.nexus.organization.Organization;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
// We redefine the table here to add multi-tenant unique constraints
@Table(name = "nexus_users", uniqueConstraints = {
        // A username must be unique *within* an organization
        @UniqueConstraint(columnNames = {"organization_id", "username"}),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This is the core of multi-tenancy.
    // Many users can belong to one organization.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY = Don't fetch the Org unless we ask for it
    @JoinColumn(name = "organization_id", nullable = false) // This is the foreign key
    private Organization organization;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // This will store the BCrypt hash

    @Column(nullable = false)
    private String role; // e.g., "ROLE_WORKER", "ROLE_ADMIN"

    // --- Constructors ---
    public User() {
    }

    // Updated constructor
    public User(Organization organization, String username, String email, String password, String role) {
        this.organization = organization;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- Getters and Setters ---
    // (We need these for JPA and mapping)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // --- equals, hashCode, and toString (Good practice) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Note: We only use ID for equality after an object is persisted.
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        // Use a constant hashcode for entities before they are persisted
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        // CRITICAL: DO NOT include 'organization' here. It will cause
        // a StackOverflowError from an infinite loop (User -> Org -> User...).
        // And NEVER include 'password'.
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", organizationId=" + (organization != null ? organization.getId() : "null") +
                '}';
    }
}
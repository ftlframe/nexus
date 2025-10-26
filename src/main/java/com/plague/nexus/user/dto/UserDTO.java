// File: src/main/java/com/plague/nexus/user/dto/UserDTO.java

package com.plague.nexus.user.dto;

import com.plague.nexus.user.User;

// This is our "safe" representation of a User.
// Notice: NO PASSWORD.
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Long organizationId;

    // We can create a constructor that maps from our Entity.
    // This is a great pattern.
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.organizationId = user.getOrganization().getId();
    }

    // --- Getters and Setters ---
    // (Auto-generate these as well)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
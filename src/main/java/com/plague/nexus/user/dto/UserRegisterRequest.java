// File: src/main/java/com/plague/nexus/user/dto/UserRegisterRequest.java

package com.plague.nexus.user.dto;

// These are from the 'Spring Boot Starter Validation'
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Note: This is a plain Java class (a "POJO" or "record").
// It has NO @Entity or @Table annotations. It is NOT a database table.
public class UserRegisterRequest {

    @NotBlank(message = "Organization name is required.")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters.")
    private String organizationName;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 12, max = 100, message = "Password must be at least 12 characters long.")
    private String password;

    // --- Getters and Setters ---
    // (IntelliJ can auto-generate these: Alt + Insert -> Getters and Setters)

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
}
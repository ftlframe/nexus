// File: src/main/java/com/yourcompany/nexus/organization/Organization.java

package com.plague.nexus.organization;

import com.plague.nexus.user.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // "Acme Inc."

    // This is the "other side" of the relationship
    // One organization can have many users.
    @OneToMany(
            mappedBy = "organization", // This "maps" to the 'organization' field in User.java
            cascade = CascadeType.ALL, // If we delete an org, delete its users
            orphanRemoval = true
    )
    private List<User> users = new ArrayList<>();

    // --- We will add palette lists, etc. here later ---

    // --- Constructors ---
    public Organization() {
    }

    public Organization(String name) {
        this.name = name;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // --- Helper methods to keep both sides of the relationship in sync ---

    public void addUser(User user) {
        users.add(user);
        user.setOrganization(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setOrganization(null);
    }

    // --- equals, hashCode, and toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
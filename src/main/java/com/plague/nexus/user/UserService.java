// File: src/main/java/com/plague/nexus/user/UserService.java

package com.plague.nexus.user;

import com.plague.nexus.organization.Organization;
import com.plague.nexus.organization.OrganizationRepository;
import com.plague.nexus.user.dto.UserRegisterRequest;
import com.plague.nexus.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    // These are our dependencies, injected by Spring
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Constructor Injection (The Best Way) ---
    @Autowired
    public UserService(UserRepository userRepository,
                       OrganizationRepository organizationRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new Organization and its first admin User.
     * This is a "transactional" operation.
     */
    @Transactional // 1. (See Tooltips)
    public UserDTO registerNewUser(UserRegisterRequest request) {

        // 2. VALIDATE: Check if org name is taken
        if (organizationRepository.findByName(request.getOrganizationName()).isPresent()) {
            // In a real app, we'd create a custom exception
            throw new IllegalArgumentException("Organization name is already taken.");
        }

        // 3. CREATE: Create the new Organization
        Organization newOrg = new Organization(request.getOrganizationName());

        // 4. HASH: Securely hash the password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 5. CREATE: Create the new User
        // The first user to register an org is automatically an admin
        User newUser = new User(
                newOrg,
                request.getUsername(),
                request.getEmail(),
                hashedPassword,
                "ROLE_ADMIN" // 6. (See Tooltips)
        );

        // 6. LINK: Link the user to the org
        newOrg.addUser(newUser);

        // 7. SAVE: Save the new Organization.
        // Because of our @OneToMany(cascade = CascadeType.ALL),
        // Spring will automatically save the newUser at the same time.
        organizationRepository.save(newOrg);

        // 8. RETURN: Return the "safe" DTO, never the entity
        return new UserDTO(newUser);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );
    }
}
// File: src/main/java/com/plague/nexus/auth/AuthController.java

package com.plague.nexus.auth;

import com.plague.nexus.shared.security.JwtTokenProvider; // <-- Import
import com.plague.nexus.user.User; // <-- Import
import com.plague.nexus.user.UserService;
import com.plague.nexus.user.dto.UserRegisterRequest;
import com.plague.nexus.user.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager; // <-- Import
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // <-- Import
import org.springframework.security.core.Authentication; // <-- Import
import org.springframework.security.core.context.SecurityContextHolder; // <-- Import
import org.springframework.security.core.userdetails.UserDetails; // <-- Import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager, // <-- Add
                          JwtTokenProvider tokenProvider) { // <-- Add
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        UserDTO newUser = userService.registerNewUser(request);
        return ResponseEntity
                .created(URI.create("/api/users/" + newUser.getId()))
                .body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. This is the "engine"
        // It uses our CustomUserDetailsService and PasswordEncoder to
        // securely check the email and password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2. If the above line doesn't throw an exception, the user is authenticated.
        // We set the authentication in the security context.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. This is a bit of a "hack" to get our *full* User object.
        // Spring's "principal" is just the email, but we need the orgId, role, etc.
        // We go back to our own service to get the full user details.
        // NOTE: This assumes your CustomUserDetailsService's UserDetails uses the email as the username
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User user = userService.findUserByEmail(principal.getUsername());


        // 4. Generate the JWT "passport"
        String jwt = tokenProvider.generateToken(
                user.getUsername(), // We use username as subject
                user.getId(),
                user.getOrganization().getId(),
                user.getRole()
        );

        // 5. Send the token back to the client
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
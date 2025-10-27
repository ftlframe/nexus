package com.plague.nexus.user;

import com.plague.nexus.core.security.JwtUtils;
import com.plague.nexus.user.dto.JwtResponse;
import com.plague.nexus.user.dto.LoginRequest;
import com.plague.nexus.user.dto.MessageResponse;
import com.plague.nexus.user.dto.RegisterRequest;
import com.plague.nexus.user.role.Role;
import com.plague.nexus.user.role.RoleRepository;
import com.plague.nexus.user.role.RoleType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    // --- LOGIN ENDPOINT ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            // 1. Authenticate the user. If successful, Spring gives us an Authentication object.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            // 2. Set the authentication in the context and generate the JWT.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            // 3. Prepare the successful response body.
            User userDetails = (User) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails));

        } catch (AuthenticationException e) {
            // 4. CRITICAL FIX: Catch authentication failure and return a clean 401 JSON response.
            // This prevents the exception from bubbling up and hitting the AuthEntryPointJwt,
            // which was causing the confusion and 401 loop.
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid Username or Password."));
        }
    }

    // --- REGISTRATION ENDPOINT ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        // Check for duplicates
        if (userRepository.existsByUsername(registerRequest.username())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user and HASH the password
        User user = new User(
                registerRequest.username(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password())
        );

        // Set roles
        Set<String> strRoles = registerRequest.roles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Default role ROLE_USER is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toUpperCase()) {
                    case "ADMIN":
                        roles.add(roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found.")));
                        break;
                    case "WAREHOUSE":
                        roles.add(roleRepository.findByName(RoleType.ROLE_WAREHOUSE)
                                .orElseThrow(() -> new RuntimeException("Error: Role WAREHOUSE is not found.")));
                        break;
                    case "ACCOUNTING":
                        roles.add(roleRepository.findByName(RoleType.ROLE_ACCOUNTING)
                                .orElseThrow(() -> new RuntimeException("Error: Role ACCOUNTING is not found.")));
                        break;
                    default:
                        roles.add(roleRepository.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found.")));
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
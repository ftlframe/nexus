package com.plague.nexus.core.config;

import com.plague.nexus.core.security.AuthEntryPointJwt;
import com.plague.nexus.core.security.AuthTokenFilter;
import com.plague.nexus.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Turns on Spring Web Security
@EnableMethodSecurity // Allows us to use @PreAuthorize on controllers later
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    // This is the "Password Hashing" bean.
    // We use BCrypt, the industry standard.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    /**
     * This "Provider" tells Spring Security to use our UserDetailsService
     * and our PasswordEncoder when it tries to authenticate a user.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // This exposes the "AuthenticationManager" as a bean.
    // We'll need this in our AuthController to run the login process.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * This is the main "Rulebook" for our security guard.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF and Session Management (Statefulness is handled later)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. Add our exception handler
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                // 3. CRITICAL ORDER: The permitAll() calls MUST come BEFORE anyRequest()
                .authorizeHttpRequests(auth -> auth
                        // AGGRESSIVE PERMIT: Allow all HTTP methods for the entire /api/auth path
                        .requestMatchers("/api/auth/**").permitAll()

                        // Allow documentation paths
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // CATCH ALL: All other requests MUST be authenticated
                        .anyRequest().authenticated()
                )

                // 4. Set the provider and add the JWT filter
                .authenticationProvider(authenticationProvider());

        // Ensure the filter runs early
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
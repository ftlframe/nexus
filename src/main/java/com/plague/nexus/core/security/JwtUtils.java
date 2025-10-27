package com.plague.nexus.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plague.nexus.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // This method generates the token
    public String generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        // 1. Define the signing algorithm using our secret
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        // 2. Create and sign the token
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                .sign(algorithm);
    }

    // This method reads a token and gets the username
    public String getUsernameFromJwtToken(String token) {
        // 1. Define the signing algorithm (must be the same)
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        // 2. Create a verifier
        JWTVerifier verifier = JWT.require(algorithm).build();

        // 3. Verify the token and decode it
        DecodedJWT decodedJWT = verifier.verify(token);

        // 4. Get the subject (username)
        return decodedJWT.getSubject();
    }

    // This validates the token
    public boolean validateJwtToken(String authToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();

            // This line does all the work:
            // It checks the signature, the expiration, and the format.
            verifier.verify(authToken);

            return true;
        } catch (JWTVerificationException e) {
            // This one exception class handles all validation errors
            logger.error("JWT verification failed: {}", e.getMessage());
        }
        return false;
    }
}
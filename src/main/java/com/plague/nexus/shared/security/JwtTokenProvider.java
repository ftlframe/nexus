// File: src/main/java/com/plague/nexus/shared/security/JwtTokenProvider.java

package com.plague.nexus.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // 1. Inject values from application.properties
    @Value("${nexus.jwt.secret}")
    private String jwtSecretString;

    @Value("${nexus.jwt.expiration-ms}")
    private long jwtExpirationInMs;

    private SecretKey jwtSecretKey;
    private JwtParser jwtParser;

    // 2. This method runs *after* the @Value fields are injected
    @PostConstruct
    public void init() {
        // 2a. Convert our Base64 string secret into a real SecretKey object
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecretString);
        this.jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);

        // 2b. Create a reusable parser that knows our key
        this.jwtParser = Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build();
    }

    /**
     * Generates a new JWT for a successfully authenticated user.
     */
    public String generateToken(String username, Long userId, Long organizationId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // 3. Create the "claims" (the data in the token)
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("orgId", organizationId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecretKey) // 4. Sign it with our secret key
                .compact();
    }

    /**
     * Validates a token's signature and expiration.
     */
    public boolean validateToken(String token) {
        try {
            // 5. Try to parse the token. If it's bad, it throws an exception.
            jwtParser.parse(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * Extracts all claims (data) from a valid token.
     */
    public Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload();
    }
}
package com.plague.nexus.shared.security;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Generates a 512-bit (64-byte) key for HS512
        SecretKey key = Jwts.SIG.HS512.key().build();

        // We must use the URL-safe encoder, as this is what our
        // provider expects.
        String base64UrlKey = Base64.getUrlEncoder().encodeToString(key.getEncoded());

        System.out.println("--- YOUR NEW 512-bit JWT SECRET KEY (Base64URL) ---");
        System.out.println(base64UrlKey);
        System.out.println("-----------------------------------------------------");
    }
}
// File: src/main/java/com/plague/nexus/auth/LoginResponse.java

package com.plague.nexus.auth;

// This is a simple "holder" object for our token.
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    // --- Getters and Setters ---
    // (Auto-generate these)

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
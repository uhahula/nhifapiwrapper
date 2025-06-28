package com.oau.nhif.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a token and its expiration information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfo {
    private final String token;
    private final Instant expiresAt;
    
    @JsonCreator
    public TokenInfo(
            @JsonProperty("token") String token,
            @JsonProperty("expiresAt") Instant expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public TokenInfo(String token, long expiresInSeconds) {
        this(token, Instant.now().plusSeconds(expiresInSeconds)); // Use actual expiry time
    }

    public String getToken() {
        return token;
    }
    
    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        // Add a 30-second buffer to avoid using tokens that are about to expire
        return Instant.now().plusSeconds(30).isAfter(expiresAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenInfo tokenInfo = (TokenInfo) o;
        return Objects.equals(token, tokenInfo.token) &&
               Objects.equals(expiresAt, tokenInfo.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expiresAt);
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
               "token='[REDACTED]'" +
               ", expiresAt=" + expiresAt +
               '}';
    }
}

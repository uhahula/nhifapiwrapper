package com.oau.nhif.client;

import lombok.Getter;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration for the NHIF API client.
 * Use the builder pattern to create an instance.
 */
@Getter
public class NhifApiConfig {
    private final String clientId;
    private final String clientSecret;
    private final String authBaseUrl;
    private final String tokenEndpoint;
    private final String serviceBaseUrl;
    private final String ocsBaseUrl;
    private final String username;
    private final Duration connectionTimeout;
    private final Duration readTimeout;
    private final int maxRetries;
    private final boolean enableLogging;
    private final boolean skipSslVerification;

    private NhifApiConfig( Builder builder) {
        this.authBaseUrl = builder.authBaseUrl;
        this.tokenEndpoint = builder.tokenEndpoint;
        this.serviceBaseUrl = builder.serviceBaseUrl;
        this.ocsBaseUrl = builder.ocsBaseUrl;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.username = builder.username;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.maxRetries = builder.maxRetries;
        this.enableLogging = builder.enableLogging;
        this.skipSslVerification = builder.skipSslVerification;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String authBaseUrl = "https://test.nhif.or.tz";
        private String tokenEndpoint = "/authserver/connect/token";
        private String serviceBaseUrl = "https://test.nhif.or.tz/servicehub";
        private String ocsBaseUrl = "https://test.nhif.or.tz/ocs";
        private String clientId;
        private String clientSecret;
        private String username;
        private Duration connectionTimeout = Duration.ofSeconds(30);
        private Duration readTimeout = Duration.ofSeconds(30);
        private int maxRetries = 3;
        private boolean enableLogging = false;
        private boolean skipSslVerification = true;  // Default to true to avoid ssl verification errors

        public Builder authBaseUrl(String baseUrl) {
            this.authBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            return this;
        }

        public Builder tokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint.startsWith("/") ? tokenEndpoint : "/" + tokenEndpoint;
            return this;
        }

        public Builder serviceBaseUrl(String baseUrl) {
            this.serviceBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            return this;
        }

        public Builder ocsBaseUrl(String baseUrl) {
            this.ocsBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = Objects.requireNonNull(clientId, "Client ID cannot be null");
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = Objects.requireNonNull(clientSecret, "Client secret cannot be null");
            return this;
        }

        public Builder username(String username) {
            this.username = Objects.requireNonNull(username, "Username cannot be null");
            return this;
        }

        public Builder connectionTimeout(Duration connectionTimeout) {
            this.connectionTimeout = Objects.requireNonNull(connectionTimeout, "Connection timeout cannot be null");
            return this;
        }

        public Builder readTimeout(Duration readTimeout) {
            this.readTimeout = Objects.requireNonNull(readTimeout, "Read timeout cannot be null");
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            if (maxRetries < 0) {
                throw new IllegalArgumentException("Max retries cannot be negative");
            }
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder enableLogging(boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }

        public Builder skipSslVerification(boolean skipSslVerification) {
            this.skipSslVerification = skipSslVerification;
            return this;
        }

        public NhifApiConfig build() {
            Objects.requireNonNull(authBaseUrl, "Auth base URL is required");
            Objects.requireNonNull(serviceBaseUrl, "Service base URL is required");
            Objects.requireNonNull(ocsBaseUrl, "OCS base URL is required");
            Objects.requireNonNull(clientId, "Client ID is required");
            Objects.requireNonNull(clientSecret, "Client secret is required");
            Objects.requireNonNull(username, "Username is required");
            return new NhifApiConfig(this);
        }
    }
}

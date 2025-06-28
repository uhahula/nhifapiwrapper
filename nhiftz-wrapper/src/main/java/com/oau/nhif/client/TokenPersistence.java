package com.oau.nhif.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

/**
 * Handles persistence of token information to disk.
 */
class TokenPersistence {
    private static final Logger logger = LoggerFactory.getLogger(TokenPersistence.class);
    private static final String TOKEN_FILE_NAME = ".nhif_token.json";
    private final ObjectMapper objectMapper;
    private final Path tokenFilePath;

    public TokenPersistence() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
        
        // Store token in user's home directory
        String userHome = System.getProperty("user.home");
        this.tokenFilePath = Paths.get(userHome, TOKEN_FILE_NAME);
        logger.debug("Token file path: {}", tokenFilePath);
    }

    /**
     * Saves the token information to disk.
     */
    public void saveToken(TokenInfo tokenInfo) {
        try {
            String json = objectMapper.writeValueAsString(tokenInfo);
            Files.writeString(tokenFilePath, json);
            logger.debug("Token saved to {}", tokenFilePath);
        } catch (IOException e) {
            logger.warn("Failed to save token to file: {}", e.getMessage());
        }
    }

    /**
     * Loads the token information from disk if it exists and is not expired.
     * @return TokenInfo if a valid token exists, null otherwise
     */
    public TokenInfo loadToken() {
        if (!Files.exists(tokenFilePath)) {
            logger.debug("No token file found at {}", tokenFilePath);
            return null;
        }

        try {
            String json = Files.readString(tokenFilePath);
            TokenInfo tokenInfo = objectMapper.readValue(json, new TypeReference<>() {});
            
            // Verify the token is not expired
            if (tokenInfo.isExpired()) {
                logger.debug("Loaded token is expired");
                return null;
            }
            
            logger.debug("Successfully loaded token from {}", tokenFilePath);
            return tokenInfo;
        } catch (IOException e) {
            logger.warn("Failed to load token from file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Deletes the token file.
     */
    public void deleteToken() {
        try {
            Files.deleteIfExists(tokenFilePath);
            logger.debug("Token file deleted: {}", tokenFilePath);
        } catch (IOException e) {
            logger.warn("Failed to delete token file: {}", e.getMessage());
        }
    }
}

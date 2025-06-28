package com.oau.nhif.client;

import com.oau.nhif.exception.NhifApiException;

/**
 * Factory class for creating NHIF API client instances
 */
public class NhifApiClientFactory {
    
    /**
     * Creates a new NHIF API client with the specified configuration
     * 
     * @param authBaseUrl The base URL for authentication (e.g., "https://verification.nhif.or.tz")
     * @param serviceBaseUrl The base URL for service endpoints (e.g., "http://test.nhif.or.tz/servicehub")
     * @param clientId The client ID for authentication
     * @param clientSecret The client secret for authentication
     * @param username The username for authentication
     * @return A new instance of NhifApiClient
     */
    public static NhifApiClient createClient(String authBaseUrl, String serviceBaseUrl, 
                                           String clientId, String clientSecret, String username) 
                                           throws NhifApiException {
        NhifApiConfig config = NhifApiConfig.builder()
                .authBaseUrl(authBaseUrl)
                .serviceBaseUrl(serviceBaseUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .build();
                
        return new DefaultNhifApiClient(config);
    }
    
    /**
     * Creates a new NHIF API client with the specified configuration and custom timeouts
     * 
     * @param authBaseUrl The base URL for authentication (e.g., "https://verification.nhif.or.tz")
     * @param serviceBaseUrl The base URL for service endpoints (e.g., "http://test.nhif.or.tz/servicehub")
     * @param clientId The client ID for authentication
     * @param clientSecret The client secret for authentication
     * @param username The username for authentication
     * @param connectTimeout Connection timeout in milliseconds
     * @param readTimeout Read timeout in milliseconds
     * @return A new instance of NhifApiClient
     */
    public static NhifApiClient createClient(String authBaseUrl, String serviceBaseUrl,
                                           String clientId, String clientSecret, String username,
                                           int connectTimeout, int readTimeout) throws NhifApiException {
        NhifApiConfig config = NhifApiConfig.builder()
                .authBaseUrl(authBaseUrl)
                .serviceBaseUrl(serviceBaseUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .connectionTimeout(java.time.Duration.ofMillis(connectTimeout))
                .readTimeout(java.time.Duration.ofMillis(readTimeout))
                .build();
                
        return new DefaultNhifApiClient(config);
    }
    
    /**
     * Creates a new NHIF API client with the provided configuration object
     * 
     * @param config The NHIF API configuration
     * @return A new instance of NhifApiClient
     */
    public static NhifApiClient createClient(NhifApiConfig config) throws NhifApiException {
        return new DefaultNhifApiClient(config);
    }
}

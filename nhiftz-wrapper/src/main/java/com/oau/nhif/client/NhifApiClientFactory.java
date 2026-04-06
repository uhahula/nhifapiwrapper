package com.oau.nhif.client;

import com.oau.nhif.exception.NhifApiException;

/**
 * Factory class for creating NHIF API client instances
 */
public class NhifApiClientFactory {

    /**
     * Creates a new NHIF API client with the specified configuration.
     * Uses the default OCS base URL derived from the service base URL pattern.
     *
     * @param authBaseUrl The base URL for authentication (e.g., "https://test.nhif.or.tz")
     * @param serviceBaseUrl The base URL for ServiceHub endpoints (e.g., "https://test.nhif.or.tz/servicehub")
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
     * Creates a new NHIF API client with separate ServiceHub and OCS base URLs.
     *
     * @param authBaseUrl The base URL for authentication (e.g., "https://test.nhif.or.tz")
     * @param serviceBaseUrl The base URL for ServiceHub endpoints (e.g., "https://test.nhif.or.tz/servicehub")
     * @param ocsBaseUrl The base URL for OCS/Claims endpoints (e.g., "https://test.nhif.or.tz/ocs")
     * @param clientId The client ID for authentication
     * @param clientSecret The client secret for authentication
     * @param username The username for authentication
     * @return A new instance of NhifApiClient
     */
    public static NhifApiClient createClient(String authBaseUrl, String serviceBaseUrl, String ocsBaseUrl,
                                           String clientId, String clientSecret, String username)
                                           throws NhifApiException {
        NhifApiConfig config = NhifApiConfig.builder()
                .authBaseUrl(authBaseUrl)
                .serviceBaseUrl(serviceBaseUrl)
                .ocsBaseUrl(ocsBaseUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .build();

        return new DefaultNhifApiClient(config);
    }

    /**
     * Creates a new NHIF API client with the specified configuration and custom timeouts
     *
     * @param authBaseUrl The base URL for authentication (e.g., "https://test.nhif.or.tz")
     * @param serviceBaseUrl The base URL for ServiceHub endpoints (e.g., "https://test.nhif.or.tz/servicehub")
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

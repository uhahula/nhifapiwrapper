package com.oau.nhif.exception;

import lombok.Getter;

/**
 * Exception thrown when there is an error communicating with the NHIF API.
 */
@Getter
public class NhifApiException extends Exception {
    private final int statusCode;
    private final String responseBody;

    public NhifApiException(String message) {
        super(message);
        this.statusCode = 0;
        this.responseBody = null;
    }

    public NhifApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.responseBody = null;
    }

    public NhifApiException(String message, int statusCode, String responseBody) {
        super(String.format("%s (Status: %d, Response: %s)", message, statusCode, responseBody));
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

}

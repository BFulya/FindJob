package com.findjob.exception;

/**
 * Custom exception for resource not found scenarios
 * Follows Exception handling pattern
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.findjob.exception;

/**
 * Custom exception for invalid credentials scenarios
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

package com.findjob.exception;

/**
 * Custom exception for unauthorized access scenarios
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}

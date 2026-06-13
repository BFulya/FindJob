package com.findjob.exception;

/**
 * Custom exception for account locked scenarios
 */
public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }
}

package com.findjob.service;

import com.findjob.dto.AuthResponse;
import com.findjob.dto.LoginRequest;
import com.findjob.dto.RegisterRequest;

/**
 * Service interface for user operations
 * Follows Interface Segregation Principle
 */
public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String username);

    void resetLoginAttempts(String username);

    void incrementLoginAttempts(String username);

    void lockAccount(String username);
}

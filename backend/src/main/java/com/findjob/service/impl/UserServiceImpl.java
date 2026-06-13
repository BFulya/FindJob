package com.findjob.service.impl;

import com.findjob.dto.AuthResponse;
import com.findjob.dto.LoginRequest;
import com.findjob.dto.RegisterRequest;
import com.findjob.entity.Admin;
import com.findjob.entity.JobSeeker;
import com.findjob.entity.Role;
import com.findjob.entity.User;
import com.findjob.exception.ResourceNotFoundException;
import com.findjob.exception.AccountLockedException;
import com.findjob.exception.InvalidCredentialsException;
import com.findjob.repository.AdminRepository;
import com.findjob.repository.JobSeekerRepository;
import com.findjob.repository.UserRepository;
import com.findjob.security.JwtTokenProvider;
import com.findjob.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService
 * Implements business logic for user authentication and registration
 * Follows Single Responsibility Principle
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register user with username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user;
        if (request.getRole() == Role.ADMIN) {
            user = createAdmin(request);
        } else {
            user = createJobSeeker(request);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        String token = jwtTokenProvider.generateToken(savedUser.getUsername(), savedUser.getRole());

        return new AuthResponse(token, savedUser.getUsername(), savedUser.getEmail(), 
                               savedUser.getRole(), savedUser.getId());
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!user.getAccountNonLocked()) {
            throw new AccountLockedException("Account is locked due to too many failed login attempts");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            incrementLoginAttempts(request.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        resetLoginAttempts(request.getUsername());
        log.info("User logged in successfully: {}", request.getUsername());

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getUsername(), user.getEmail(), 
                               user.getRole(), user.getId());
    }

    @Override
    @Transactional
    public void logout(String username) {
        log.info("User logged out: {}", username);
    }

    @Override
    @Transactional
    public void resetLoginAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setLoginAttempts(0);
        user.setAccountNonLocked(true);
        userRepository.save(user);
        log.info("Login attempts reset for user: {}", username);
    }

    @Override
    @Transactional
    public void incrementLoginAttempts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setLoginAttempts(user.getLoginAttempts() + 1);
        
        if (user.getLoginAttempts() >= 3) {
            user.setAccountNonLocked(false);
            log.warn("Account locked for user: {} due to too many failed attempts", username);
        }
        
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void lockAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setAccountNonLocked(false);
        userRepository.save(user);
        log.warn("Account locked for user: {}", username);
    }

    private Admin createAdmin(RegisterRequest request) {
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setCompanyName(request.getCompanyName());
        admin.setCompanyAddress(request.getCompanyAddress());
        admin.setTaxNumber(request.getTaxNumber());
        return admin;
    }

    private JobSeeker createJobSeeker(RegisterRequest request) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setUsername(request.getUsername());
        jobSeeker.setFirstName(request.getFirstName());
        jobSeeker.setLastName(request.getLastName());
        jobSeeker.setEmail(request.getEmail());
        jobSeeker.setPhoneNumber(request.getPhoneNumber());
        jobSeeker.setBirthDate(request.getBirthDate());
        jobSeeker.setMilitaryStatus(request.getMilitaryStatus());
        jobSeeker.setYearsOfExperience(request.getYearsOfExperience());
        jobSeeker.setEducation(request.getEducation());
        jobSeeker.setSkills(request.getSkills());
        jobSeeker.setWorkExperience(request.getWorkExperience());
        jobSeeker.setPortfolioUrl(request.getPortfolioUrl());
        jobSeeker.setAvailableForWork(request.getAvailableForWork());
        jobSeeker.setExpectedSalary(request.getExpectedSalary());
        return jobSeeker;
    }
}

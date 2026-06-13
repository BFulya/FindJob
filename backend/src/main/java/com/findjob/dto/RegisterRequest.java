package com.findjob.dto;

import com.findjob.entity.MilitaryStatus;
import com.findjob.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for registration requests
 * Includes validation for all required fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private Role role;

    // Job Seeker specific fields
    private LocalDate birthDate;

    private MilitaryStatus militaryStatus;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private String education;

    private String skills;

    private String workExperience;

    private String portfolioUrl;

    private Boolean availableForWork = true;

    @DecimalMin(value = "0.0", message = "Expected salary cannot be negative")
    private Double expectedSalary;

    // Admin specific fields
    private String companyName;

    private String companyAddress;

    private String taxNumber;
}

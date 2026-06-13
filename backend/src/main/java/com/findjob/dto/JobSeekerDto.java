package com.findjob.dto;

import com.findjob.entity.MilitaryStatus;
import com.findjob.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for JobSeeker entity
 * Used for displaying job seeker information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDate birthDate;
    private MilitaryStatus militaryStatus;
    private Integer yearsOfExperience;
    private String education;
    private String skills;
    private String workExperience;
    private String portfolioUrl;
    private Boolean availableForWork;
    private Double expectedSalary;
}

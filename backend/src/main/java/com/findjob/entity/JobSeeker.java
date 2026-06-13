package com.findjob.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * JobSeeker entity representing job seeking users
 * Inherits from User base class with additional job-related properties
 */
@Entity
@Table(name = "job_seekers")
@DiscriminatorValue("JOB_SEEKER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"appliedJobs"})
@EqualsAndHashCode(callSuper = true, exclude = {"appliedJobs"})
public class JobSeeker extends User {

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilitaryStatus militaryStatus;

    @Column(nullable = false)
    private Integer yearsOfExperience;

    @Column(nullable = false)
    private String education;

    @Column(length = 2000)
    private String skills;

    @Column(length = 2000)
    private String workExperience;

    @Column(length = 1000)
    private String portfolioUrl;

    @Column(nullable = false)
    private Boolean availableForWork = true;

    @Column(nullable = false)
    private Double expectedSalary;

    @ManyToMany(mappedBy = "applicants")
    private Set<Job> appliedJobs = new HashSet<>();
}

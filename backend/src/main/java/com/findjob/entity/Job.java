package com.findjob.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Job entity representing job postings by companies
 * Implements many-to-many relationship with JobSeeker
 */
@Entity
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"postedBy", "applicants"})
@EqualsAndHashCode(exclude = {"postedBy", "applicants"})
public class Job extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Double salaryMin;

    @Column(nullable = false)
    private Double salaryMax;

    @Column(nullable = false)
    private Integer requiredExperience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilitaryStatus requiredMilitaryStatus;

    @Column(nullable = false)
    private String requiredEducation;

    @Column(length = 1000)
    private String requiredSkills;

    @Column(nullable = false)
    private LocalDate postingDate;

    @Column(nullable = false)
    private LocalDate applicationDeadline;

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_id", nullable = false)
    private Admin postedBy;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "job_applications",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "job_seeker_id")
    )
    private Set<JobSeeker> applicants = new HashSet<>();
}

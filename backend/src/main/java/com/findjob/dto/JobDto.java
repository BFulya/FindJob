package com.findjob.dto;

import com.findjob.entity.MilitaryStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for Job entity
 * Used for creating and updating job postings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location must not exceed 200 characters")
    private String location;

    @NotBlank(message = "Department is required")
    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @NotNull(message = "Minimum salary is required")
    @DecimalMin(value = "0.0", message = "Minimum salary cannot be negative")
    private Double salaryMin;

    @NotNull(message = "Maximum salary is required")
    @DecimalMin(value = "0.0", message = "Maximum salary cannot be negative")
    private Double salaryMax;

    @NotNull(message = "Required experience is required")
    @Min(value = 0, message = "Required experience cannot be negative")
    private Integer requiredExperience;

    @NotNull(message = "Required military status is required")
    private MilitaryStatus requiredMilitaryStatus;

    @NotBlank(message = "Required education is required")
    private String requiredEducation;

    @Size(max = 1000, message = "Required skills must not exceed 1000 characters")
    private String requiredSkills;

    @NotNull(message = "Application deadline is required")
    @Future(message = "Application deadline must be in the future")
    private LocalDate applicationDeadline;

    private Long postedById;
    private String postedByCompanyName;
    private LocalDate postingDate;
    private Boolean isActive;
}

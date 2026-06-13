package com.findjob.dto;

import com.findjob.entity.MilitaryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for job seeker filtering requests
 * Used by admins to filter candidates
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerFilterRequest {

    private MilitaryStatus militaryStatus;
    private Integer minExperience;
    private Double maxSalary;
    private String education;
    private Integer page = 0;
    private Integer size = 10;
}

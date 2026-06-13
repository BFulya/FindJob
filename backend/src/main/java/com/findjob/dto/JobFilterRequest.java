package com.findjob.dto;

import com.findjob.entity.MilitaryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for job filtering requests
 * Used for searching and filtering jobs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobFilterRequest {

    private String location;
    private String department;
    private Double minSalary;
    private Integer maxExperience;
    private MilitaryStatus militaryStatus;
    private Integer page = 0;
    private Integer size = 10;
}

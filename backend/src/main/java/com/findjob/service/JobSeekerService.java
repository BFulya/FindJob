package com.findjob.service;

import com.findjob.dto.JobSeekerDto;
import com.findjob.dto.JobSeekerFilterRequest;
import org.springframework.data.domain.Page;

/**
 * Service interface for job seeker operations
 * Follows Interface Segregation Principle
 */
public interface JobSeekerService {

    JobSeekerDto getJobSeekerById(Long id);

    JobSeekerDto getJobSeekerByUsername(String username);

    Page<JobSeekerDto> filterJobSeekers(JobSeekerFilterRequest filterRequest);

    void saveToFile(Long jobSeekerId);

    JobSeekerDto updateJobSeeker(Long id, JobSeekerDto jobSeekerDto);
}

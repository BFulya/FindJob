package com.findjob.service;

import com.findjob.dto.JobDto;
import com.findjob.dto.JobFilterRequest;
import org.springframework.data.domain.Page;

/**
 * Service interface for job operations
 * Follows Interface Segregation Principle
 */
public interface JobService {

    JobDto createJob(JobDto jobDto, Long adminId);

    JobDto updateJob(Long jobId, JobDto jobDto, Long adminId);

    void deleteJob(Long jobId, Long adminId);

    JobDto getJobById(Long jobId);

    Page<JobDto> filterJobs(JobFilterRequest filterRequest);

    Page<JobDto> getJobsByAdmin(Long adminId, int page, int size);

    void applyForJob(Long jobId, Long jobSeekerId);
}

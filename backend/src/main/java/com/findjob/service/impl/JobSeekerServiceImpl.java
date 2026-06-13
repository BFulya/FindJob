package com.findjob.service.impl;

import com.findjob.dto.JobSeekerDto;
import com.findjob.dto.JobSeekerFilterRequest;
import com.findjob.entity.JobSeeker;
import com.findjob.exception.ResourceNotFoundException;
import com.findjob.repository.JobSeekerRepository;
import com.findjob.service.JobSeekerService;
import com.findjob.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of JobSeekerService
 * Implements business logic for job seeker operations
 * Follows Single Responsibility Principle
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobSeekerServiceImpl implements JobSeekerService {

    private final JobSeekerRepository jobSeekerRepository;
    private final FileStorageUtil fileStorageUtil;

    @Override
    public JobSeekerDto getJobSeekerById(Long id) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        return convertToDto(jobSeeker);
    }

    @Override
    public JobSeekerDto getJobSeekerByUsername(String username) {
        JobSeeker jobSeeker = jobSeekerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        return convertToDto(jobSeeker);
    }

    @Override
    public Page<JobSeekerDto> filterJobSeekers(JobSeekerFilterRequest filterRequest) {
        log.info("Filtering job seekers with criteria: {}", filterRequest);

        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<JobSeeker> jobSeekers = jobSeekerRepository.filterJobSeekers(
                filterRequest.getMilitaryStatus(),
                filterRequest.getMinExperience(),
                filterRequest.getMaxSalary(),
                filterRequest.getEducation(),
                pageable
        );

        return jobSeekers.map(this::convertToDto);
    }

    @Override
    @Transactional
    public void saveToFile(Long jobSeekerId) {
        log.info("Saving job seeker data to file for ID: {}", jobSeekerId);

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));

        fileStorageUtil.saveJobSeekerToFile(jobSeeker);
        log.info("Job seeker data saved to file successfully");
    }

    @Override
    @Transactional
    public JobSeekerDto updateJobSeeker(Long id, JobSeekerDto jobSeekerDto) {
        log.info("Updating job seeker with ID: {}", id);

        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));

        jobSeeker.setFirstName(jobSeekerDto.getFirstName());
        jobSeeker.setLastName(jobSeekerDto.getLastName());
        jobSeeker.setPhoneNumber(jobSeekerDto.getPhoneNumber());
        jobSeeker.setMilitaryStatus(jobSeekerDto.getMilitaryStatus());
        jobSeeker.setYearsOfExperience(jobSeekerDto.getYearsOfExperience());
        jobSeeker.setEducation(jobSeekerDto.getEducation());
        jobSeeker.setSkills(jobSeekerDto.getSkills());
        jobSeeker.setWorkExperience(jobSeekerDto.getWorkExperience());
        jobSeeker.setPortfolioUrl(jobSeekerDto.getPortfolioUrl());
        jobSeeker.setAvailableForWork(jobSeekerDto.getAvailableForWork());
        jobSeeker.setExpectedSalary(jobSeekerDto.getExpectedSalary());

        JobSeeker updatedJobSeeker = jobSeekerRepository.save(jobSeeker);
        log.info("Job seeker updated successfully with ID: {}", updatedJobSeeker.getId());

        return convertToDto(updatedJobSeeker);
    }

    private JobSeekerDto convertToDto(JobSeeker jobSeeker) {
        JobSeekerDto dto = new JobSeekerDto();
        dto.setId(jobSeeker.getId());
        dto.setUsername(jobSeeker.getUsername());
        dto.setFirstName(jobSeeker.getFirstName());
        dto.setLastName(jobSeeker.getLastName());
        dto.setEmail(jobSeeker.getEmail());
        dto.setPhoneNumber(jobSeeker.getPhoneNumber());
        dto.setRole(jobSeeker.getRole());
        dto.setBirthDate(jobSeeker.getBirthDate());
        dto.setMilitaryStatus(jobSeeker.getMilitaryStatus());
        dto.setYearsOfExperience(jobSeeker.getYearsOfExperience());
        dto.setEducation(jobSeeker.getEducation());
        dto.setSkills(jobSeeker.getSkills());
        dto.setWorkExperience(jobSeeker.getWorkExperience());
        dto.setPortfolioUrl(jobSeeker.getPortfolioUrl());
        dto.setAvailableForWork(jobSeeker.getAvailableForWork());
        dto.setExpectedSalary(jobSeeker.getExpectedSalary());
        return dto;
    }
}

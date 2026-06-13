package com.findjob.service.impl;

import com.findjob.dto.JobDto;
import com.findjob.dto.JobFilterRequest;
import com.findjob.entity.Admin;
import com.findjob.entity.Job;
import com.findjob.entity.JobSeeker;
import com.findjob.exception.ResourceNotFoundException;
import com.findjob.exception.UnauthorizedException;
import com.findjob.repository.AdminRepository;
import com.findjob.repository.JobRepository;
import com.findjob.repository.JobSeekerRepository;
import com.findjob.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Implementation of JobService
 * Implements business logic for job operations
 * Follows Single Responsibility Principle
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final AdminRepository adminRepository;
    private final JobSeekerRepository jobSeekerRepository;

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, Long adminId) {
        log.info("Creating job with title: {} by admin: {}", jobDto.getTitle(), adminId);

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setLocation(jobDto.getLocation());
        job.setDepartment(jobDto.getDepartment());
        job.setSalaryMin(jobDto.getSalaryMin());
        job.setSalaryMax(jobDto.getSalaryMax());
        job.setRequiredExperience(jobDto.getRequiredExperience());
        job.setRequiredMilitaryStatus(jobDto.getRequiredMilitaryStatus());
        job.setRequiredEducation(jobDto.getRequiredEducation());
        job.setRequiredSkills(jobDto.getRequiredSkills());
        job.setApplicationDeadline(jobDto.getApplicationDeadline());
        job.setPostingDate(LocalDate.now());
        job.setIsActive(true);
        job.setPostedBy(admin);

        Job savedJob = jobRepository.save(job);
        log.info("Job created successfully with ID: {}", savedJob.getId());

        return convertToDto(savedJob);
    }

    @Override
    @Transactional
    public JobDto updateJob(Long jobId, JobDto jobDto, Long adminId) {
        log.info("Updating job with ID: {} by admin: {}", jobId, adminId);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getId().equals(adminId)) {
            throw new UnauthorizedException("You are not authorized to update this job");
        }

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setLocation(jobDto.getLocation());
        job.setDepartment(jobDto.getDepartment());
        job.setSalaryMin(jobDto.getSalaryMin());
        job.setSalaryMax(jobDto.getSalaryMax());
        job.setRequiredExperience(jobDto.getRequiredExperience());
        job.setRequiredMilitaryStatus(jobDto.getRequiredMilitaryStatus());
        job.setRequiredEducation(jobDto.getRequiredEducation());
        job.setRequiredSkills(jobDto.getRequiredSkills());
        job.setApplicationDeadline(jobDto.getApplicationDeadline());
        job.setIsActive(jobDto.getIsActive());

        Job updatedJob = jobRepository.save(job);
        log.info("Job updated successfully with ID: {}", updatedJob.getId());

        return convertToDto(updatedJob);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId, Long adminId) {
        log.info("Deleting job with ID: {} by admin: {}", jobId, adminId);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getId().equals(adminId)) {
            throw new UnauthorizedException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
        log.info("Job deleted successfully with ID: {}", jobId);
    }

    @Override
    public JobDto getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return convertToDto(job);
    }

    @Override
    public Page<JobDto> filterJobs(JobFilterRequest filterRequest) {
        log.info("Filtering jobs with criteria: {}", filterRequest);

        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                Sort.by("postingDate").descending()
        );

        Page<Job> jobs = jobRepository.filterJobs(
                filterRequest.getLocation(),
                filterRequest.getDepartment(),
                filterRequest.getMinSalary(),
                filterRequest.getMaxExperience(),
                filterRequest.getMilitaryStatus(),
                pageable
        );

        return jobs.map(this::convertToDto);
    }

    @Override
    public Page<JobDto> getJobsByAdmin(Long adminId, int page, int size) {
        log.info("Getting jobs for admin with ID: {}", adminId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("postingDate").descending());
        Page<Job> jobs = jobRepository.findByPostedById(adminId, pageable);

        return jobs.map(this::convertToDto);
    }

    @Override
    @Transactional
    public void applyForJob(Long jobId, Long jobSeekerId) {
        log.info("Job seeker {} applying for job {}", jobSeekerId, jobId);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));

        if (job.getApplicants().contains(jobSeeker)) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        job.getApplicants().add(jobSeeker);
        jobRepository.save(job);

        log.info("Job seeker {} successfully applied for job {}", jobSeekerId, jobId);
    }

    private JobDto convertToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setDepartment(job.getDepartment());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setRequiredExperience(job.getRequiredExperience());
        dto.setRequiredMilitaryStatus(job.getRequiredMilitaryStatus());
        dto.setRequiredEducation(job.getRequiredEducation());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setPostingDate(job.getPostingDate());
        dto.setIsActive(job.getIsActive());
        dto.setPostedById(job.getPostedBy().getId());
        dto.setPostedByCompanyName(job.getPostedBy().getCompanyName());
        return dto;
    }
}

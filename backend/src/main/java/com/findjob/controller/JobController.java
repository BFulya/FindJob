package com.findjob.controller;

import com.findjob.dto.JobDto;
import com.findjob.dto.JobFilterRequest;
import com.findjob.entity.Role;
import com.findjob.security.JwtTokenProvider;
import com.findjob.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for job operations
 * Follows RESTful API design principles
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Job management APIs")
public class JobController {

    private final JobService jobService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new job", description = "Creates a new job posting (Admin only)")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobDto jobDto,
                                           @RequestHeader("Authorization") String token) {
        Long adminId = getUserIdFromToken(token);
        return ResponseEntity.ok(jobService.createJob(jobDto, adminId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a job", description = "Updates an existing job posting (Admin only)")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long id,
                                          @Valid @RequestBody JobDto jobDto,
                                          @RequestHeader("Authorization") String token) {
        Long adminId = getUserIdFromToken(token);
        return ResponseEntity.ok(jobService.updateJob(id, jobDto, adminId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a job", description = "Deletes a job posting (Admin only)")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        Long adminId = getUserIdFromToken(token);
        jobService.deleteJob(id, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieves a job by its ID")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/filter")
    @Operation(summary = "Filter jobs", description = "Filters jobs based on criteria")
    public ResponseEntity<Page<JobDto>> filterJobs(@RequestBody JobFilterRequest filterRequest) {
        return ResponseEntity.ok(jobService.filterJobs(filterRequest));
    }

    @GetMapping("/admin/{adminId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get jobs by admin", description = "Retrieves all jobs posted by a specific admin")
    public ResponseEntity<Page<JobDto>> getJobsByAdmin(@PathVariable Long adminId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.getJobsByAdmin(adminId, page, size));
    }

    @PostMapping("/{jobId}/apply")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @Operation(summary = "Apply for a job", description="Job seeker applies for a job")
    public ResponseEntity<Void> applyForJob(@PathVariable Long jobId,
                                           @RequestHeader("Authorization") String token) {
        Long jobSeekerId = getUserIdFromToken(token);
        jobService.applyForJob(jobId, jobSeekerId);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromToken(String token) {
        String jwt = token.substring(7);
        String username = jwtTokenProvider.getUsernameFromToken(jwt);
        return Long.parseLong(username);
    }
}

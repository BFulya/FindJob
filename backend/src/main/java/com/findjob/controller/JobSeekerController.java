package com.findjob.controller;

import com.findjob.dto.JobSeekerDto;
import com.findjob.dto.JobSeekerFilterRequest;
import com.findjob.security.JwtTokenProvider;
import com.findjob.service.JobSeekerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for job seeker operations
 * Follows RESTful API design principles
 */
@RestController
@RequestMapping("/api/job-seekers")
@RequiredArgsConstructor
@Tag(name = "Job Seekers", description = "Job seeker management APIs")
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{id}")
    @Operation(summary = "Get job seeker by ID", description = "Retrieves a job seeker by their ID")
    public ResponseEntity<JobSeekerDto> getJobSeekerById(@PathVariable Long id) {
        return ResponseEntity.ok(jobSeekerService.getJobSeekerById(id));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @Operation(summary = "Get current job seeker profile", description = "Retrieves the current job seeker's profile")
    public ResponseEntity<JobSeekerDto> getCurrentJobSeeker(@RequestHeader("Authorization") String token) {
        String username = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        return ResponseEntity.ok(jobSeekerService.getJobSeekerByUsername(username));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @Operation(summary = "Update job seeker profile", description = "Updates a job seeker's profile")
    public ResponseEntity<JobSeekerDto> updateJobSeeker(@PathVariable Long id,
                                                         @Valid @RequestBody JobSeekerDto jobSeekerDto) {
        return ResponseEntity.ok(jobSeekerService.updateJobSeeker(id, jobSeekerDto));
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Filter job seekers", description = "Filters job seekers based on criteria (Admin only)")
    public ResponseEntity<Page<JobSeekerDto>> filterJobSeekers(@RequestBody JobSeekerFilterRequest filterRequest) {
        return ResponseEntity.ok(jobSeekerService.filterJobSeekers(filterRequest));
    }

    @PostMapping("/{id}/save-to-file")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save job seeker to file", description = "Saves job seeker data to a file (Admin only)")
    public ResponseEntity<Void> saveJobSeekerToFile(@PathVariable Long id) {
        jobSeekerService.saveToFile(id);
        return ResponseEntity.ok().build();
    }
}

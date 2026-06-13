package com.findjob.util;

import com.findjob.entity.JobSeeker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for file I/O operations
 * Implements Singleton pattern for file operations
 */
@Component
@Slf4j
public class FileStorageUtil {

    @Value("${file.storage.path}")
    private String storagePath;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void saveJobSeekerToFile(JobSeeker jobSeeker) {
        File directory = new File(storagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = storagePath + File.separator + "job_seeker_" + jobSeeker.getId() + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("=== JOB SEEKER PROFILE ===");
            writer.newLine();
            writer.write("ID: " + jobSeeker.getId());
            writer.newLine();
            writer.write("Username: " + jobSeeker.getUsername());
            writer.newLine();
            writer.write("Full Name: " + jobSeeker.getFirstName() + " " + jobSeeker.getLastName());
            writer.newLine();
            writer.write("Email: " + jobSeeker.getEmail());
            writer.newLine();
            writer.write("Phone: " + jobSeeker.getPhoneNumber());
            writer.newLine();
            writer.write("Birth Date: " + jobSeeker.getBirthDate());
            writer.newLine();
            writer.write("Military Status: " + jobSeeker.getMilitaryStatus());
            writer.newLine();
            writer.write("Years of Experience: " + jobSeeker.getYearsOfExperience());
            writer.newLine();
            writer.write("Education: " + jobSeeker.getEducation());
            writer.newLine();
            writer.write("Skills: " + jobSeeker.getSkills());
            writer.newLine();
            writer.write("Work Experience: " + jobSeeker.getWorkExperience());
            writer.newLine();
            writer.write("Portfolio URL: " + jobSeeker.getPortfolioUrl());
            writer.newLine();
            writer.write("Available for Work: " + jobSeeker.getAvailableForWork());
            writer.newLine();
            writer.write("Expected Salary: " + jobSeeker.getExpectedSalary());
            writer.newLine();
            writer.write("Created At: " + jobSeeker.getCreatedAt().format(DATE_FORMATTER));
            writer.newLine();
            writer.write("Updated At: " + jobSeeker.getUpdatedAt().format(DATE_FORMATTER));
            writer.newLine();
            writer.write("===========================");
            
            log.info("Job seeker data saved to file: {}", fileName);
        } catch (IOException e) {
            log.error("Error saving job seeker data to file", e);
            throw new RuntimeException("Failed to save job seeker data to file", e);
        }
    }
}

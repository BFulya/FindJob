package com.findjob.repository;

import com.findjob.entity.Job;
import com.findjob.entity.MilitaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Job entity
 * Includes custom queries for filtering and searching
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByPostedById(Long adminId, Pageable pageable);

    Page<Job> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    Page<Job> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
           "j.isActive = true AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:department IS NULL OR LOWER(j.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +
           "(:minSalary IS NULL OR j.salaryMin >= :minSalary) AND " +
           "(:maxExperience IS NULL OR j.requiredExperience <= :maxExperience) AND " +
           "(:militaryStatus IS NULL OR j.requiredMilitaryStatus = :militaryStatus)")
    Page<Job> filterJobs(
            @Param("location") String location,
            @Param("department") String department,
            @Param("minSalary") Double minSalary,
            @Param("maxExperience") Integer maxExperience,
            @Param("militaryStatus") MilitaryStatus militaryStatus,
            Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.isActive = true AND j.applicationDeadline >= CURRENT_DATE")
    Page<Job> findActiveJobs(Pageable pageable);
}

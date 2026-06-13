package com.findjob.repository;

import com.findjob.entity.JobSeeker;
import com.findjob.entity.MilitaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for JobSeeker entity
 * Includes custom queries for filtering and searching
 */
@Repository
public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {

    Optional<JobSeeker> findByUsername(String username);

    Optional<JobSeeker> findByEmail(String email);

    Page<JobSeeker> findByMilitaryStatus(MilitaryStatus militaryStatus, Pageable pageable);

    Page<JobSeeker> findByYearsOfExperienceGreaterThanEqual(Integer years, Pageable pageable);

    Page<JobSeeker> findByAvailableForWork(Boolean available, Pageable pageable);

    @Query("SELECT js FROM JobSeeker js WHERE " +
           "(:militaryStatus IS NULL OR js.militaryStatus = :militaryStatus) AND " +
           "(:minExperience IS NULL OR js.yearsOfExperience >= :minExperience) AND " +
           "(:maxSalary IS NULL OR js.expectedSalary <= :maxSalary) AND " +
           "(:education IS NULL OR LOWER(js.education) LIKE LOWER(CONCAT('%', :education, '%')))")
    Page<JobSeeker> filterJobSeekers(
            @Param("militaryStatus") MilitaryStatus militaryStatus,
            @Param("minExperience") Integer minExperience,
            @Param("maxSalary") Double maxSalary,
            @Param("education") String education,
            Pageable pageable
    );
}

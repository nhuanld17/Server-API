package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    public Optional<Application> findByCandidateAndJob(Candidate candidate, Job job);
    
    @Query("SELECT a FROM Application a WHERE a.job.id = :jobId AND (:fullname IS NULL OR a.candidate.fullName LIKE %:fullname%)")
    Page<Application> findApplicationsByJobIdAndFullname(
            @Param("jobId") int jobId,
            @Param("fullname") String fullname,
            Pageable pageable
    );
}

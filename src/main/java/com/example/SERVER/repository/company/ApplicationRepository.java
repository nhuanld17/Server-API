package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    public Optional<Application> findByCandidateAndJob(Candidate candidate, Job job);
}

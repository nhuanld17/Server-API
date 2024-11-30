package com.example.SERVER.service.company;

import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.repository.company.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Optional<Application> findApplicationToUpdate(Candidate candidate, Job job){
        return this.applicationRepository.findByCandidateAndJob(candidate, job);
    }

    @Transactional
    public void applyJob(Application application){
        applicationRepository.save(application);
    }
}

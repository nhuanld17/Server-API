package com.example.SERVER.service.company;

import com.example.SERVER.domain.dto.application.ApplicationDTO;
import com.example.SERVER.domain.dto.common.Meta;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.repository.company.ApplicationRepository;
import com.example.SERVER.service.job.JobService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobService jobService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobService jobService) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
    }

    public Optional<Application> findApplicationToUpdate(Candidate candidate, Job job){
        return this.applicationRepository.findByCandidateAndJob(candidate, job);
    }

    @Transactional
    public void applyJob(Application application){
        applicationRepository.save(application);
    }
    
    public ResultPaginationDTO handleSearchApplication(int jobId, String fullname, Pageable sortedPage) {
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        
        Page<Application> applicationPage = this.applicationRepository.findApplicationsByJobIdAndFullname(jobId, fullname, sortedPage);
        
        meta.setPage(sortedPage.getPageNumber() + 1);
        meta.setPageSize(sortedPage.getPageSize());
        meta.setPages(applicationPage.getTotalPages());
        meta.setTotal(applicationPage.getTotalElements());
        
        resultPaginationDTO.setMeta(meta);
        
        List<ApplicationDTO> applicationDTOS = applicationPage.getContent()
                .stream().map(application -> new ApplicationDTO(
                        application.getCandidate().getId(),
                        application.getId(),
                        application.getCandidate().getPictureProfileLink(),
                        application.getCandidate().getFullName(),
                        application.getCandidate().getTitle(),
                        application.getCandidate().getCandidateDetail().getExperience(),
                        application.getCandidate().getCandidateDetail().getEducation(),
                        application.getCandidate().getCandidateDetail().getEmail(),
                        application.getCvLink()
                )).toList();
        
        resultPaginationDTO.setResult(applicationDTOS);
        resultPaginationDTO.setMeta(meta);
        
        return resultPaginationDTO;
    }
    
    public Application getApplicationById(Long id) {
        return applicationRepository.getReferenceById(id);
    }
}

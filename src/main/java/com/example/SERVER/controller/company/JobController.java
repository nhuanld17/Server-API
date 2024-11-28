package com.example.SERVER.controller.company;

import com.example.SERVER.domain.dto.job.JobDTO;
import com.example.SERVER.domain.dto.job.JobDetailsDTO;
import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.CompanyDetail;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.util.exception.custom.IdInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public ResponseEntity<List<JobDTO>> useGetAllJob(){
        List<Job> jobs = jobService.findAllJob();
        ArrayList<JobDTO> listJob = new ArrayList<>();
        for (Job job : jobs) {
            JobDTO jobDTO = new JobDTO(
              job.getId(),
              job.getTitle(),
              job.getTags(),
              job.getJobType(),
              job.getCompany().getCompanyDetail().getProfilePictureLink(),
              job.getMaxSalary()
            );
            listJob.add(jobDTO);
        }
        return ResponseEntity.ok().body(listJob);
    }
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobDetailsDTO> useGetJobDetail(@PathVariable long id) throws IdInvalidException {
        Optional<Job> job = jobService.findJobDetail(id);
        if (job.isEmpty()) {
            throw new IdInvalidException("Id khong hop le");
        }

        Company company = job.get().getCompany();
        CompanyDetail companyDetail = company.getCompanyDetail();

        LocalDate expirationDate = job.get().getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate postAt = job.get().getPostAt().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateEstablished = companyDetail.getDateEstablished().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String expirationDateF = expirationDate.format(formatter);
        String postAtF = postAt.format(formatter);
        String dateEstablishedF = dateEstablished.format(formatter);

        JobDetailsDTO jobDetailsDTO = new JobDetailsDTO(
                job.get().getId(),
                job.get().getTitle(),
                job.get().getTags(),
                job.get().getMaxSalary(),
                job.get().getEducation(),
                job.get().getExperience(),
                job.get().getJobType(),
                job.get().getJobRole(),
                expirationDateF,
                job.get().getJobLevel(),
                job.get().getDescription(),
                job.get().getResponesibility(),
                postAtF,
                company.getCompanyName(),
                company.getPhone(),
                company.getWebsite(),
                company.getEmail(),
                dateEstablishedF,
                company.getCompanyDetail().getProfilePictureLink(),
                company.getCompanyDetail().getTeamSize()
        );
        return ResponseEntity.ok().body(jobDetailsDTO);
    }
}

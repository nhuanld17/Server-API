package com.example.SERVER.controller.candidate;

import com.example.SERVER.domain.dto.candidate.CandidateApplyJob;
import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.service.company.ApplicationService;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/candidate")
public class ApplyJobController {
    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplyJobController(ApplicationService applicationService, JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @PostMapping("/application")
    public ResponseEntity<String> applyJob(@RequestBody CandidateApplyJob candidateApplyJob){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();
        Optional<Job> job = jobService.findJobDetail(Long.parseLong(candidateApplyJob.idJob()));


        if (applicationService.findApplicationToUpdate(candidate, job.get()).isEmpty()){
            Application application = new Application(
                    0,
                    candidateApplyJob.cvLink(),
                    candidate,
                    job.get()
            );
            applicationService.applyJob(application);
        } else {
            Optional<Application> application = applicationService.findApplicationToUpdate(candidate, job.get());
            application.get().setJob(job.get());
            application.get().setCandidate(candidate);
            application.get().setCvLink(candidateApplyJob.cvLink());
            applicationService.applyJob(application.get());
        }
        return ResponseEntity.ok().body("apply thanh cong");
    }
}

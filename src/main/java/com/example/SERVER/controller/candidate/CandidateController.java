package com.example.SERVER.controller.candidate;

import com.example.SERVER.domain.dto.candidate.*;
import com.example.SERVER.domain.dto.job.JobApplyDTO;
import com.example.SERVER.domain.dto.job.JobWishListDTO;
import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.candidate.LinkSocial;
import com.example.SERVER.domain.entity.company.Application;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.service.canditate.CandidateService;
import com.example.SERVER.service.canditate.LinkSocialService;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.service.user.UserService;
import com.example.SERVER.util.exception.custom.IsEmtyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    private final CandidateService candidateService;
    private final UserService userService;
    private final LinkSocialService linkSocialService;
    private final JobService jobService;

    public CandidateController(CandidateService candidateService, UserService userService, LinkSocialService linkSocialService, JobService jobService) {
        this.candidateService = candidateService;
        this.userService = userService;
        this.linkSocialService = linkSocialService;
        this.jobService = jobService;
    }

    @GetMapping("/basic")
    public ResponseEntity<CandidateBasicDTO> getCandidateBasic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        String formattedDate = null;

        if (candidate.getCandidateDetail().getBirthDate() != null) {
            Date birthDate = candidate.getCandidateDetail().getBirthDate();
            LocalDate localDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formattedDate = localDate.format(formatter);
        }

        CandidateBasicDTO candidateBasicDTO = new CandidateBasicDTO(
                candidate.getFullName(),
                candidate.getTitle(),
                candidate.getCandidateDetail().getGender(),
                formattedDate,
                candidate.getPortfolio(),
                candidate.getPictureProfileLink()
        );
        return ResponseEntity.ok().body(candidateBasicDTO);
    }
    @GetMapping("/profile")
    public ResponseEntity<CandidateDetailDTO> getCandidateDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        CandidateDetailDTO candidateDetailDTO = new CandidateDetailDTO(
                candidate.getCandidateDetail().getEducation(),
                candidate.getCandidateDetail().getExperience(),
                candidate.getCandidateDetail().getBio()
        );
        return ResponseEntity.ok().body(candidateDetailDTO);
    }

    @PostMapping("/basic/update")
    public ResponseEntity<Candidate> updateCandidateBasic(@RequestBody CandidateBasicDTO candidateBasicDTO) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        String dateString = candidateBasicDTO.dateOfBirth();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dateString);

        candidate.setFullName(candidateBasicDTO.fullName());
        candidate.setTitle(candidateBasicDTO.title());
        candidate.getCandidateDetail().setGender(candidateBasicDTO.gender());
        candidate.getCandidateDetail().setBirthDate(date);
        candidate.setPortfolio(candidateBasicDTO.porfolio());
        candidate.setPictureProfileLink(candidateBasicDTO.pictureProfileLink());

        candidateService.updateCandidate(candidate);
        return ResponseEntity.ok().body(candidate);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Candidate> updateCandidateDetail(@RequestBody CandidateDetailDTO candidateDetailDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        candidate.getCandidateDetail().setEducation(candidateDetailDTO.education());
        candidate.getCandidateDetail().setExperience(candidateDetailDTO.experience());
        candidate.getCandidateDetail().setBio(candidateDetailDTO.bio());

        candidateService.updateCandidate(candidate);
        return ResponseEntity.ok().body(candidate);
    }

    @GetMapping("/social-link")
    public ResponseEntity<CandidatSocialLinkDTO> getCandidateSocialLink(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        CandidatSocialLinkDTO candidatSocialLinkDTO = new CandidatSocialLinkDTO(
                candidate.getLinkSocial().getFbLink(),
                candidate.getLinkSocial().getTwitter_link(),
                candidate.getLinkSocial().getLinked_link()
        );

        return ResponseEntity.ok().body(candidatSocialLinkDTO);
    }

    @PostMapping("/social-link/update")
    public ResponseEntity<CandidatSocialLinkDTO> updateCandidateSocialLink(@RequestBody CandidatSocialLinkDTO candidatSocialLinkDTO) throws IsEmtyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        LinkSocial linkSocial = candidate.getLinkSocial();
        if (candidatSocialLinkDTO == null) {
            throw new IsEmtyException("doi tuong rong");
        }
        linkSocial.setFbLink(candidatSocialLinkDTO.facebookLink());
        linkSocial.setTwitter_link(candidatSocialLinkDTO.twitterLink());
        linkSocial.setLinked_link(candidatSocialLinkDTO.linkedLink());

        linkSocialService.saveLinkSocial(linkSocial);
        return ResponseEntity.ok().body(candidatSocialLinkDTO);
    }

    @GetMapping("/contact")
    public ResponseEntity<CandidateSettingDTO> getCandidateContact(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        CandidateSettingDTO candidateSettingDTO = new CandidateSettingDTO(
                candidate.getCandidateDetail().getLocation(),
                candidate.getCandidateDetail().getPhoneNumber(),
                candidate.getCandidateDetail().getEmail()
        );

        return ResponseEntity.ok().body(candidateSettingDTO);
    }

    @PostMapping("/contact/update")
    public ResponseEntity<String> updateCandidateContact(@RequestBody CandidateContactDTO candidateContactDTO) throws IsEmtyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        if (candidateContactDTO == null) {
            throw new IsEmtyException("doi tuong update rong");
        }

        if ((candidateContactDTO.currentPassword() != null) && (candidateContactDTO.newPassword() != null)) {

        }
        candidate.getCandidateDetail().setLocation(candidateContactDTO.location());
        candidate.getCandidateDetail().setPhoneNumber(candidateContactDTO.phoneNumber());
        candidate.getCandidateDetail().setEmail(candidateContactDTO.email());

        candidateService.updateCandidate(candidate);

        return ResponseEntity.ok().body("cap nhat thanh cong");
    }

    @PostMapping("/wish-list")
    public ResponseEntity<String> addWishList(@RequestParam String jobId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();
        Optional<Job> job = jobService.findJobDetail(Long.parseLong(jobId));

        if (candidate.getCandidateWishList().getJobs().contains(job.get())){
            candidate.getCandidateWishList().getJobs().remove(job.get());
        }
        else if (candidate.getCandidateWishList().getJobs() == null) {
            List<Job> jobs = new ArrayList<>();
            jobs.add(job.get());
            candidate.getCandidateWishList().setJobs(jobs);
        }
        else {
            candidate.getCandidateWishList().getJobs().add(job.get());
        }

        this.candidateService.updateCandidate(candidate);

        return ResponseEntity.ok().body("them thanh cong");
    }

    @GetMapping("/application/list")
    public ResponseEntity<List<JobApplyDTO>> getListJobApply(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        List<JobApplyDTO> jobApplyDTOList = new ArrayList<>();

        for (Application application : candidate.getApplications()) {
            jobApplyDTOList.add(new JobApplyDTO(
               application.getJob().getId(),
               application.getJob().getTitle(),
               application.getJob().getJobType(),
               application.getJob().getCompany().getCompanyDetail().getProfilePictureLink(),
               application.getJob().getMaxSalary()
            ));
        }

        return ResponseEntity.ok().body(jobApplyDTOList);
    }

    @GetMapping("/wish-list")
    public ResponseEntity<List<JobWishListDTO>> getJobWishList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Candidate candidate = userService.handleGetUserByUsername(authentication.getName()).getCandidate();

        List<JobWishListDTO> jobWishListDTOs = new ArrayList<>();

        for (Job job : candidate.getCandidateWishList().getJobs()) {
            jobWishListDTOs.add(new JobWishListDTO(
                    job.getId(),
                    job.getTitle(),
                    job.getJobType(),
                    job.getCompany().getCompanyDetail().getProfilePictureLink(),
                    job.getMaxSalary()
            ));
        }

        return ResponseEntity.ok().body(jobWishListDTOs);
    }

}

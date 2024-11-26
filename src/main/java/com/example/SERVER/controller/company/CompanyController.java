package com.example.SERVER.controller.company;

import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.service.user.UserService;
import com.example.SERVER.util.exception.custom.JobNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {
	
	private final UserService userService;
	private final JobService jobService;
	
	public CompanyController(UserService userService, JobService jobService) {
		this.userService = userService;
		this.jobService = jobService;
	}
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@PostMapping("/create-job")
	public ResponseEntity<Job> createJob(@RequestBody Job job){
		
		// Lấy email từ SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication.getName());
		
		// Lấy user tương ứng với email
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		// Lấy company từ User
		Company company = currentUser.getCompany();
		
		// Liên kết job với company
		job.setCompany(company);
		
		// Lưu Job của company - liên kết company với job
		if (company.getJobs() == null) {
			company.setJobs(new ArrayList<>());
		}
		company.getJobs().add(job);
		
		
		this.jobService.saveJob(job);
		
		return ResponseEntity.ok().body(job);
	}
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@GetMapping("/list-job")
	public ResponseEntity<List<Job>> getListJob() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		
		List<Job> jobs = company.getJobs();
		
		return ResponseEntity.status(HttpStatus.OK).body(jobs);
	}
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@GetMapping("/job/{id}")
	public ResponseEntity<Job> getJobById(@PathVariable long id) throws JobNotExistException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		Job job = company.getJobs().stream().filter(j -> j.getId() == id).findFirst().orElse(null);
		
		if (job == null) {
			throw new JobNotExistException("Bài đăng không tồn tại");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(job);
	}
}

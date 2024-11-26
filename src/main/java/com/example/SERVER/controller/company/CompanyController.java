package com.example.SERVER.controller.company;

import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.service.company.CompanyService;
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
	private final CompanyService companyService;
	
	public CompanyController(
			UserService userService, JobService jobService,
			CompanyService companyService) {
		this.userService = userService;
		this.jobService = jobService;
		this.companyService = companyService;
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
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@PutMapping("/job/{id}")
	public ResponseEntity<Job> updateJob(@PathVariable long id, @RequestBody Job job) throws JobNotExistException {
		// Lấy thông tin người dùng hiện tại từ SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = userService.handleGetUserByUsername(authentication.getName());
		
		// Lấy thông tin của người dùng
		Company company = currentUser.getCompany();
		
		// Tìm kiếm job trong danh sách công ty
		Job existingJob = company.getJobs()
				.stream()
				.filter(j -> j.getId() == id).findFirst().orElseThrow(() -> new JobNotExistException("Job không tồn tại"));
		
		existingJob.setTitle(job.getTitle());
		existingJob.setTags(job.getTags());
		existingJob.setMaxSalary(job.getMaxSalary());
		existingJob.setEducation(job.getEducation());
		existingJob.setExperience(job.getExperience());
		existingJob.setJobType(job.getJobType());
		existingJob.setJobRole(job.getJobRole());
		existingJob.setExpirationDate(job.getExpirationDate());
		existingJob.setJobLevel(job.getJobLevel());
		existingJob.setDescription(job.getDescription());
		existingJob.setResponesibility(job.getResponesibility());
		
		// Lưu lại công ty với job đã cập nhật
		companyService.saveCompany(company);
		
		return ResponseEntity.status(HttpStatus.OK).body(existingJob);
	}
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@DeleteMapping("/job/{id}")
	public ResponseEntity<Job> deleteJob(@PathVariable long id) throws JobNotExistException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		Job job = company.getJobs().stream().filter(j -> j.getId() == id).findFirst().orElse(null);
		
		if (job == null) {
			throw new JobNotExistException("Job không tồn tại hoặc đã bị xóa trước đó");
		}
		
		// Xóa job khỏi danh sách job của company
		company.getJobs().remove(job);
		
		// Lưu lại công ty
		this.companyService.saveCompany(company);
		// Hoặc: this.jobRepository.delete(job); // Xóa trực tiếp từ JobRepository
		
		return ResponseEntity.status(HttpStatus.OK).body(job);
	}
}

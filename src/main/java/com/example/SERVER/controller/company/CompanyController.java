package com.example.SERVER.controller.company;

import com.example.SERVER.domain.dto.company.CompanyInfoDTO;
import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.service.company.CompanyService;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.service.user.UserService;
import com.example.SERVER.util.exception.custom.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/company")
public class CompanyController {
	
	private final UserService userService;
	private final JobService jobService;
	private final CompanyService companyService;
	
	public CompanyController(UserService userService, JobService jobService, CompanyService companyService) {
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

	@GetMapping("/list-job")
		@PreAuthorize("hasRole('ROLE_COMPANY')")
	public ResponseEntity<List<Job>> getListJob() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		
		List<Job> jobs = company.getJobs();
		
		return ResponseEntity.status(HttpStatus.OK).body(jobs);
	}

	@GetMapping("/info/{id}")
	public ResponseEntity<CompanyInfoDTO> getCompanyInFO(@PathVariable long id) throws IdInvalidException {
		Optional<Company> company = companyService.getCompany(id);
		if (company.isEmpty()) {
			throw new IdInvalidException("id khong hop le");
		}
		Date dateEstablished = company.get().getCompanyDetail().getDateEstablished();
		LocalDate localDate = dateEstablished.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = localDate.format(formatter);


		CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO(
				company.get().getId(),
				company.get().getCompanyName(),
				company.get().getPhone(),
				company.get().getWebsite(),
				company.get().getEmail(),
				formattedDate,
				company.get().getCompanyDetail().getProfilePictureLink(),
				company.get().getCompanyDetail().getTeamSize()
		);
		return ResponseEntity.ok().body(companyInfoDTO);
	}
}

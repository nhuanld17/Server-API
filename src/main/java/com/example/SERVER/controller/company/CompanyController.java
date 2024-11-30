package com.example.SERVER.controller.company;

import com.example.SERVER.domain.dto.Job.CompanyJobDTO;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.dto.company.ContactInfoDTO;
import com.example.SERVER.domain.dto.company.FoundingInfoDTO;
import com.example.SERVER.domain.dto.company.ResCompanyInfoDTO;
import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.CompanyDetail;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.domain.entity.user.User;
import com.example.SERVER.service.company.CloudinaryService;
import com.example.SERVER.service.company.CompanyService;
import com.example.SERVER.service.job.JobService;
import com.example.SERVER.service.user.UserService;
import com.example.SERVER.util.exception.custom.JobNotExistException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
public class CompanyController {
	
	private final UserService userService;
	private final JobService jobService;
	private final CompanyService companyService;
	private final CloudinaryService cloudinaryService;
	
	public CompanyController(
			UserService userService, JobService jobService,
			CompanyService companyService, CloudinaryService cloudinaryService) {
		this.userService = userService;
		this.jobService = jobService;
		this.companyService = companyService;
		this.cloudinaryService = cloudinaryService;
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
	public ResponseEntity<ResultPaginationDTO> getListJob(
			@RequestParam(defaultValue = "postAt") String sortField,
			@RequestParam(defaultValue = "desc") String sortDirection,
			Pageable pageable
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		Company company = currentUser.getCompany();
		
		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		
		ResultPaginationDTO resultPaginationDTO = this.companyService.getCompanyJob(company.getId(), sortedPageable);
		
		return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
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
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@GetMapping("/info")
	public ResponseEntity<ResCompanyInfoDTO> getCompanyInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		ResCompanyInfoDTO companyInfoDTO = new ResCompanyInfoDTO();
		companyInfoDTO.setCompanyName(company.getCompanyName());
		companyInfoDTO.setImgLink(company.getCompanyDetail().getProfilePictureLink());
		companyInfoDTO.setAboutUs(company.getCompanyDetail().getAboutUs());
		
		return ResponseEntity.status(HttpStatus.OK).body(companyInfoDTO);
	}
	
//	@PreAuthorize("hasRole('ROLE_COMPANY')")
//	@PutMapping("/update-info")
//	public ResponseEntity<ResCompanyInfoDTO> updateCompanyInfo(
//			@RequestParam("companyName") String companyName,
//			@RequestParam("aboutUs") String aboutUs,
//			@RequestParam("imageFile") MultipartFile imageFile) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
//
//		Company company = currentUser.getCompany();
//		CompanyDetail companyDetail = company.getCompanyDetail();
//
//		company.setCompanyName(companyName);
//		companyDetail.setAboutUs(aboutUs);
//
//		// Nếu ảnh không được cập nhật, thì lấy lại link cũ
//		if (imageFile == null || imageFile.isEmpty()) {
//			companyDetail.setProfilePictureLink(company.getCompanyDetail().getProfilePictureLink());
//		} else {
//			// Nếu thay đôi ảnh, xóa ảnh cũ ở cloud và upload lên cloud ảnh mới
//			// Xóa ảnh cũ nếu có
//			String oldImageUrl = company.getCompanyDetail().getProfilePictureLink();
//			if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
//				this.cloudinaryService.delete(oldImageUrl, "CompanyAvatar");
//			}
//
//			Map data = this.cloudinaryService.upload(imageFile, "CompanyAvatar");
//			String pictureProfileLink = (String) data.get("secure_url");
//			companyDetail.setProfilePictureLink(pictureProfileLink);
//		}
//
//		// Lưu lại
//		company.setCompanyDetail(companyDetail);
//		companyService.saveCompany(company);
//
//		// Tạo ResCompanyInfoDTO để trả về lại thông tin đã cập nhật
//		// để front end set lại giá trị của các ô input
//		ResCompanyInfoDTO companyInfoDTO = new ResCompanyInfoDTO();
//		companyInfoDTO.setCompanyName(company.getCompanyName());
//		companyInfoDTO.setAboutUs(company.getCompanyDetail().getAboutUs());
//		companyInfoDTO.setImgLink(company.getCompanyDetail().getProfilePictureLink());
//
//		return ResponseEntity.status(HttpStatus.OK).body(companyInfoDTO);
//	}
	
	// Dành cho tab Company info
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@PutMapping("/update-info")
	public ResponseEntity<ResCompanyInfoDTO> updateCompanyInfo(@RequestBody ResCompanyInfoDTO companyInfoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		CompanyDetail companyDetail = company.getCompanyDetail();
		
		company.setCompanyName(companyInfoDTO.getCompanyName());
		companyDetail.setAboutUs(companyInfoDTO.getAboutUs());
		
		if (companyInfoDTO.getImgLink() == null) {
			companyDetail.setProfilePictureLink(companyDetail.getProfilePictureLink());
		} else {
			companyDetail.setProfilePictureLink(companyInfoDTO.getImgLink());
		}
		
		company.setCompanyDetail(companyDetail);
		
		companyService.saveCompany(company);
		
		return ResponseEntity.status(HttpStatus.OK).body(companyInfoDTO);
	}
	
	// trả về thông tin cho tab founding info
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@GetMapping("/founding-info")
	public ResponseEntity<FoundingInfoDTO> getFoundingInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		CompanyDetail companyDetail = company.getCompanyDetail();
		
		// Thêm thông tin vào FoudingInfoDTO để trả về dữ liệu
		FoundingInfoDTO foundingInfoDTO = new FoundingInfoDTO();
		foundingInfoDTO.setTeamSize(companyDetail.getTeamSize());
		foundingInfoDTO.setCompanyWebSite(company.getWebsite());
		foundingInfoDTO.setIndustryType(companyDetail.getIndustryType());
		foundingInfoDTO.setYearOfEstablishment(companyDetail.getDateEstablished());
		
		
		return ResponseEntity.ok().body(foundingInfoDTO);
	}
	
	// Cập nhật thông tin ở tab founding info
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@PutMapping("/update-founding-info")
	public ResponseEntity<FoundingInfoDTO> updateFoundingInfo(@RequestBody FoundingInfoDTO foundingInfoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		CompanyDetail companyDetail = company.getCompanyDetail();
		
		companyDetail.setIndustryType(foundingInfoDTO.getIndustryType());
		companyDetail.setTeamSize(foundingInfoDTO.getTeamSize());
		companyDetail.setDateEstablished(foundingInfoDTO.getYearOfEstablishment());
		company.setWebsite(foundingInfoDTO.getCompanyWebSite());
		
		company.setCompanyDetail(companyDetail);
		
		companyService.saveCompany(company);
		
		return ResponseEntity.status(HttpStatus.OK).body(foundingInfoDTO);
	}
	
	// Get thông tin liên hệ ở tab Account setting
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@GetMapping("/contact-info")
	public ResponseEntity<ContactInfoDTO> getContactInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		CompanyDetail companyDetail = company.getCompanyDetail();
		
		ContactInfoDTO contactInfoDTO = new ContactInfoDTO();
		contactInfoDTO.setEmail(company.getEmail());
		contactInfoDTO.setLocation(companyDetail.getLocation());
		contactInfoDTO.setPhoneNumber(company.getPhone());
		
		return ResponseEntity.ok().body(contactInfoDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_COMPANY')")
	@PutMapping("/update-contact-info")
	public ResponseEntity<ContactInfoDTO> updateContactInfo(@RequestBody ContactInfoDTO contactInfoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userService.handleGetUserByUsername(authentication.getName());
		
		Company company = currentUser.getCompany();
		CompanyDetail companyDetail = company.getCompanyDetail();
		
		company.setEmail(contactInfoDTO.getEmail());
		companyDetail.setLocation(contactInfoDTO.getLocation());
		company.setPhone(contactInfoDTO.getPhoneNumber());
		
		company.setCompanyDetail(companyDetail);
		this.companyService.saveCompany(company);
		
		return ResponseEntity.ok().body(contactInfoDTO);
	}
}

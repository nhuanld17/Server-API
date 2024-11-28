package com.example.SERVER.controller.job;

import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.service.job.JobService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {
	
	private final JobService jobService;
	
	@PreAuthorize("hasRole('ROLE_CANDIDATE')")
	@GetMapping("")
	public ResponseEntity<ResultPaginationDTO> searchJob(
		@Filter Specification<Job> specs,
		Pageable pageable
	) {
		ResultPaginationDTO paginationDTO = this.jobService.handleFetchAllJobs(specs, pageable);
		return ResponseEntity.ok().body(paginationDTO);
	}
}

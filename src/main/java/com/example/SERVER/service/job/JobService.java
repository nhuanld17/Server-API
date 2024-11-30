package com.example.SERVER.service.job;


import com.example.SERVER.domain.dto.job.JobSummaryDTO;
import com.example.SERVER.domain.dto.common.Meta;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.repository.company.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
	private final JobRepository jobRepository;
	
	public JobService(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}
	
	@Transactional
	public void saveJob(Job job) {
		jobRepository.save(job);
	}

	@Transactional
	public List<Job> findAllJob() {
		return this.jobRepository.findAll();
	}
	@Transactional
	public Optional<Job> findJobDetail(long id) {
		Optional<Job> job = jobRepository.findById(id);
		return job;
	}
	
	public ResultPaginationDTO handleFetchAllJobs(Specification<Job> specs, Pageable pageable) {
		ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
		Meta meta = new Meta();
		
		Page<Job> pageJob = jobRepository.findAll(specs, pageable);
		
		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		
		meta.setPages(pageJob.getTotalPages());
		meta.setTotal(pageJob.getTotalElements());
		
		resultPaginationDTO.setMeta(meta);
		List<JobSummaryDTO> jobSummaryDTOS = pageJob.getContent()
				.stream().map(job -> new JobSummaryDTO(
						job.getCompany().getCompanyDetail().getProfilePictureLink(),
						job.getTitle(),
						job.getJobType(),
						job.getCompany().getCompanyDetail().getLocation()
				)).collect(Collectors.toList());
		
		resultPaginationDTO.setResult(jobSummaryDTOS);
		
		return resultPaginationDTO;
	}
}

package com.example.SERVER.service.job;

import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.repository.company.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}

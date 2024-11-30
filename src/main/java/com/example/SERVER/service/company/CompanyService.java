package com.example.SERVER.service.company;

import com.example.SERVER.domain.dto.job.CompanyJobDTO;
import com.example.SERVER.domain.dto.common.Meta;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.Job;
import com.example.SERVER.repository.company.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
	
	private final CompanyRepository companyRepository;
	
	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}
	
	@Transactional
	public void saveCompany(Company company) {
		companyRepository.save(company);
	}
	
	public ResultPaginationDTO getCompanyJob(int id, Pageable pageable) {
		ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
		Meta meta = new Meta();
		
		Page<Job> pageJob = companyRepository.findJobsByCompanyId((long) id, pageable);
		
		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(pageJob.getTotalPages());
		meta.setTotal(pageJob.getTotalElements());
		
		resultPaginationDTO.setMeta(meta);
		
		List<CompanyJobDTO> companyJobDTOS = pageJob.getContent()
				.stream().map(job -> new CompanyJobDTO(
						job.getId(),
						job.getTitle(),
						job.getJobType(),
						job.isActive(),
						job.getNumberOfApplications(),
						job.getDaysUntilExpiration()
				)).toList();
		
		resultPaginationDTO.setResult(companyJobDTOS);
		
		return resultPaginationDTO;
	}
    public Optional<Company> getCompany(long id){
        return companyRepository.findById(id);
    }

    public List<Company> findAllCompany(){
        return this.companyRepository.findAll();
    }

}

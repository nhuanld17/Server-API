package com.example.SERVER.service.company;

import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.repository.company.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
}

package com.example.SERVER.service.company;

import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.repository.company.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Optional<Company> getCompany(long id){
        return companyRepository.findById(id);
    }

}

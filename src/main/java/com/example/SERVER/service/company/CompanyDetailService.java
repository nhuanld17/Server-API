package com.example.SERVER.service.company;

import com.example.SERVER.domain.entity.company.CompanyDetail;
import com.example.SERVER.repository.company.CompanyDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyDetailService {
    private final CompanyDetailRepository companyDetailRepository;

    public CompanyDetailService(CompanyDetailRepository companyDetailRepository) {
        this.companyDetailRepository = companyDetailRepository;
    }
    public Optional<CompanyDetail> getCompanyDetail(long id){
        return companyDetailRepository.findById(id);
    }
}

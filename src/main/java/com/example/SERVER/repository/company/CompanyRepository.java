package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}

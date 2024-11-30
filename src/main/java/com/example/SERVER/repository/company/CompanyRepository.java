package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.company.Company;
import com.example.SERVER.domain.entity.company.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	@Query("select j from Job j where j.company.id = :companyId")
	Page<Job> findJobsByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
}

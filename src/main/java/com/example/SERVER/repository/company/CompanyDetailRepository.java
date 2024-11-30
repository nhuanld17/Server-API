package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.company.CompanyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, Long> {

}

package com.example.SERVER.repository.company;

import com.example.SERVER.domain.entity.company.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

}

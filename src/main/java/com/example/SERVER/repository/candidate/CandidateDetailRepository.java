package com.example.SERVER.repository.candidate;

import com.example.SERVER.domain.entity.candidate.CandidateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateDetailRepository extends JpaRepository<CandidateDetail, Long> {

}

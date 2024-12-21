package com.example.SERVER.repository.candidate;

import com.example.SERVER.domain.entity.candidate.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	Page<Candidate> findByFullNameContaining(String candidateName, Pageable pageable);
}

package com.example.SERVER.service.canditate;

import com.example.SERVER.domain.dto.candidate.CandidateIntroDTO;
import com.example.SERVER.domain.dto.common.Meta;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.repository.candidate.CandidateRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Optional<Candidate> findCandidateById(long id){
        return candidateRepository.findById(id);
    }

    @Transactional
    public void updateCandidate(Candidate candidate){
        this.candidateRepository.save(candidate);
    }
    public List<Candidate> findAllCandidate(){
        return this.candidateRepository.findAll();
    }
}

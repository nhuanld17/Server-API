package com.example.SERVER.service.canditate;

import com.example.SERVER.domain.dto.candidate.CandidateIntroDTO;
import com.example.SERVER.domain.dto.common.Meta;
import com.example.SERVER.domain.dto.common.ResultPaginationDTO;
import com.example.SERVER.domain.entity.candidate.CandidateDetail;
import com.example.SERVER.repository.candidate.CandidateDetailRepository;
import com.example.SERVER.util.specification.CandidateDetailSpecification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateDetailService {
	
	private final CandidateDetailRepository candidateDetailRepository;
	
	public CandidateDetailService(CandidateDetailRepository candidateDetailRepository) {
		this.candidateDetailRepository = candidateDetailRepository;
	}
	
	public ResultPaginationDTO handleSearchCandidate(String fullname, String experience, String education, String gender, Pageable sortedPage) {
		ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
		Meta meta = new Meta();
		
		// Tạo Specification dựa trên các tham số lọc
		Specification<CandidateDetail> spec = CandidateDetailSpecification.withFilters(fullname, experience, education, gender);
		
		Page<CandidateDetail> candidateDetailPage = candidateDetailRepository.findAll(spec, sortedPage);
		
		
		meta.setPage(sortedPage.getPageNumber() + 1);
		meta.setPageSize(sortedPage.getPageSize());
		meta.setPages(candidateDetailPage.getTotalPages());
		meta.setTotal(candidateDetailPage.getTotalElements());
		
		
		// Xử lí meta
		List<CandidateIntroDTO> candidateIntroDTOS = candidateDetailPage.getContent()
				.stream().map(candidateDetail -> new CandidateIntroDTO(
						candidateDetail.getCandidate().getId(),
						candidateDetail.getCandidate().getFullName(),
						candidateDetail.getCandidate().getTitle(),
						candidateDetail.getLocation(),
						candidateDetail.getCandidate().getPictureProfileLink(),
						candidateDetail.getExperience()
				)).toList();
		
		resultPaginationDTO.setMeta(meta);
		resultPaginationDTO.setResult(candidateIntroDTOS);
		
		return resultPaginationDTO;
	}
	
}

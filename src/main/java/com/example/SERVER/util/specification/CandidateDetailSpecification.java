package com.example.SERVER.util.specification;

import com.example.SERVER.domain.entity.candidate.Candidate;
import com.example.SERVER.domain.entity.candidate.CandidateDetail;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CandidateDetailSpecification {

	public static Specification<CandidateDetail>
	withFilters(String q, String experience, String education, String gender) {
		return (root , query, criteriaBuilder) -> {
			
			Predicate finalPredicate = criteriaBuilder.conjunction(); // mặc định là chưa có điều kiện gì
			
			// Kết nối với bảng Candidate để truy vấn fullName
			Join<CandidateDetail, Candidate> candidateJoin = root.join("candidate", JoinType.INNER);
			
			// Kiểm tra và thêm điều kiêện cho q: từ khóa tìm kiếm
			if (q != null && !q.isEmpty()) {
				finalPredicate = criteriaBuilder.and(finalPredicate,
						criteriaBuilder.like(candidateJoin.get("fullName"), "%" + q + "%"));
			}
			
			
			// Kiểm tra và thêm điều kiện cho experience
			if (experience != null && !experience.isEmpty()) {
				finalPredicate = criteriaBuilder.and(finalPredicate,
						criteriaBuilder.like(root.get("experience"), "%" + experience + "%"));
			}
			
			// Kiểm tra và thêm điều kiện cho education
			if (education != null && !education.isEmpty()) {
				finalPredicate = criteriaBuilder.and(finalPredicate,
						criteriaBuilder.like(root.get("education"), "%" + education + "%"));
			}
			
			// Kiểm tra và thêm điều kiện cho gender
			if (gender != null && !gender.isEmpty()) {
				finalPredicate = criteriaBuilder.and(finalPredicate,
						criteriaBuilder.equal(root.get("gender"), gender));
			}
			
			return finalPredicate;
		};
	}

}

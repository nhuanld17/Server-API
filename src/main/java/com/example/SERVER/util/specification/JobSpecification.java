package com.example.SERVER.util.specification;

import com.example.SERVER.domain.entity.company.Job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> withFilters(String filter, String experience, String jobType) {
        return (root, query, criteriaBuilder) -> {

            // Khởi tạo Predicate mặc định
            Predicate finalPredicate = criteriaBuilder.conjunction();

            // Kiểm tra và thêm điều kiện cho "filter" (tìm kiếm theo tiêu đề công việc)
            if (filter != null && !filter.isEmpty()) {
                finalPredicate = criteriaBuilder.and(finalPredicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filter.toLowerCase() + "%"));
            }

            // Kiểm tra và thêm điều kiện cho "experience" (lọc theo kinh nghiệm)
            if (experience != null && !experience.isEmpty()) {
                finalPredicate = criteriaBuilder.and(finalPredicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("experience")), "%" + experience.toLowerCase() + "%"));
            }

            // Kiểm tra và thêm điều kiện cho "typeJob" (lọc theo loại công việc)
            if (jobType != null && !jobType.isEmpty()) {
                finalPredicate = criteriaBuilder.and(finalPredicate,
                        criteriaBuilder.equal(root.get("jobType"), jobType));
            }

            return finalPredicate;
        };
    }
}

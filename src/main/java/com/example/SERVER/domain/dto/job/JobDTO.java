package com.example.SERVER.domain.dto.job;

public record JobDTO(
        int id,
        String title,
        String tags,
        String jobType,
        String profilePictureLink,
        Double maxSalary
) {
}

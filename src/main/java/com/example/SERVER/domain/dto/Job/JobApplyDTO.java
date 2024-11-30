package com.example.SERVER.domain.dto.job;

public record JobApplyDTO(
        int id,
        String title,
        String jobType,
        String profilePictureLink,
        double maxSalary
) {
}

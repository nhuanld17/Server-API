package com.example.SERVER.domain.dto.job;

public record JobDetailsDTO(
        int id,
        String title,
        String tags,
        Double maxSalary,
        String education,
        String experience,
        String jobType,
        String jobRole,
        String expirationDate,
        String jobLevel,
        String description,
        String responsibility,
        String postAt,
        String companyName,
        String phone,
        String website,
        String email,
        String dateEstablished,
        String profilePictureLink,
        String teamSize
) {
}

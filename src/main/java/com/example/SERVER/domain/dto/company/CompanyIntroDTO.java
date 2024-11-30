package com.example.SERVER.domain.dto.company;

public record CompanyIntroDTO(
        int id,
        String companyName,
        String teamSize,
        int numberJobs,
        String profilePictureLink
) {
}

package com.example.SERVER.domain.dto.candidate;

public record CandidateBasicDTO(
        String fullName,
        String title,
        int gender,
        String dateOfBirth,
        String porfolio,
        String pictureProfileLink
) {
}

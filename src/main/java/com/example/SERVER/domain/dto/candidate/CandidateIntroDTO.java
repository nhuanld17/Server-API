package com.example.SERVER.domain.dto.candidate;

public record CandidateIntroDTO(
        int id,
        String fullName,
        String title,
        String location,
        String pictureProfileLink,
        String experience
) {
}

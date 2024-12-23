package com.example.SERVER.domain.dto.candidate;

public record CandidateCheckDetailDTO(
        int id,
        String fullName,
        String title,
        String portfolio,
        String resumeLink,
        String birthDate,
        int gender,
        String education,
        String experience,
        String bio,
        String location,
        String phoneNumber,
        String email,
        String facebookLink,
        String twitterLink,
        String linkedLink,
        String pictureProfileLink
) {
}

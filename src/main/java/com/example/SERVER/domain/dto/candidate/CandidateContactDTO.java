package com.example.SERVER.domain.dto.candidate;

public record CandidateContactDTO(
        String location,
        String phoneNumber,
        String email,
        String currentPassword,
        String newPassword
) {
}

package com.example.SERVER.domain.dto.company;

public record CompanyDetailDTO(
        int id,
        String companyName,
        String phone,
        String website,
        String email,
        String dateEstablished,
        String profilePictureLink,
        String teamSize,
        String description,
        String aboutUs
) {
}

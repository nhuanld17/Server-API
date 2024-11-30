package com.example.SERVER.domain.dto.company;

public record CompanyInfoDTO(
        int id,
        String companyName,
        String phone,
        String website,
        String email,
        String dateEstablished,
        String profilePictureLink,
        String teamSize
) {
}

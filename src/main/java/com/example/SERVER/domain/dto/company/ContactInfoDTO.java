package com.example.SERVER.domain.dto.company;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDTO {
	private String phoneNumber;
	private String email;
	private String location;
}

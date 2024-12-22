package com.example.SERVER.domain.dto.application;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApplicationDTO {
	private int candidateId;
	private int applicationId;
	private String candidatePictureUrl;
	private String candidateName;
	private String candidateTitle;
	private String experience;
	private String education;
	private String email;
	private String cvLink;
}

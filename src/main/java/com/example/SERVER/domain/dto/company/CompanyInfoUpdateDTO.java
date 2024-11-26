package com.example.SERVER.domain.dto.company;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CompanyInfoUpdateDTO {
	private MultipartFile imageFile;
	private String companyName;
	private String aboutUs;
}

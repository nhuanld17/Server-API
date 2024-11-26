package com.example.SERVER.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegisterDTO {
	private String companyName;
	private String companyPhone;
	private String companyEmail;
	private String companyPassword;
	private String companyPasswordConfirm;
}

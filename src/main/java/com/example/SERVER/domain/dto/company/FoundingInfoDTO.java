package com.example.SERVER.domain.dto.company;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class FoundingInfoDTO {
	private String industryType;
	private String teamSize;
	private Date yearOfEstablishment;
	private String companyWebSite;
}

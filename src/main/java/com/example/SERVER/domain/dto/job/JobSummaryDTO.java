package com.example.SERVER.domain.dto.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSummaryDTO {
	private String companyImageLink;
	private String jobTitle;
	private String jobType;
	private String location;
}

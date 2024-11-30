package com.example.SERVER.domain.dto.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyJobDTO {
	private int id;
	private String jobTitle;
	private String jobType;
	private boolean isActive;
	private long numberOfApplications;
	private long dateRemain;
}

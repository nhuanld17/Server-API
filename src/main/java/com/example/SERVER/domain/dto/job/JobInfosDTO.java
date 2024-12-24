package com.example.SERVER.domain.dto.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobInfosDTO {
	private int id;
	private String title;
	private String tags;
	private Double maxSalary;
	private String education;
	private String experience;
	private String jobType;
	private String jobRole;
	private Instant expirationDate;
	private String jobLevel;
	private String description;
	private String responsibility;
}

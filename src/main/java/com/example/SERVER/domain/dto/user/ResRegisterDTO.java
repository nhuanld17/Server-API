package com.example.SERVER.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResRegisterDTO {
	private String email;
	private String firstname;
	private String lastname;
	private String rolename;
}

package com.example.SERVER.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	private String email;
	private String password;
}

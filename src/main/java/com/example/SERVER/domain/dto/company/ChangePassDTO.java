package com.example.SERVER.domain.dto.company;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePassDTO {
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
}

package com.example.SERVER.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
	@JsonProperty("access_token")
	private String accessToken;
	private UserLogin userLogin;
	
	
	// Tạo inner class để trả về thêm thông tin của người đăng nhập
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserLogin {
		private long id;
		private String email;
		private String roleName;
	}
}

package com.farm2biz.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//What the client sends us at POST /users/login
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;
	
}

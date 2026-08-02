package com.farm2biz.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResp {

	private String token;
	private Long userId;
	private String name;
	private String email;
	private String role;
	
}

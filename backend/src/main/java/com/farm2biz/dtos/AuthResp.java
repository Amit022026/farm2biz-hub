package com.farm2biz.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//What we send back after a SUCCESSFUL login - the "wristband" (token)
//plus a little context so the frontend immediately knows who's logged
//in and what they're allowed to do, without decoding the token itself.
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

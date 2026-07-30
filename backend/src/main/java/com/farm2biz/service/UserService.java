package com.farm2biz.service;


import java.util.List;

import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.AuthRequest;
import com.farm2biz.dtos.AuthResp;
import com.farm2biz.dtos.UserDTO;

public interface UserService {

	UserDTO createUser(UserDTO dto);// now also does password hashing (see impl) - this IS "register"
	UserDTO getUserById(Long userId);
	List getAllUsers();
	UserDTO updateUser(Long userId, UserDTO dto);
	ApiResponse deleteUser(Long userId);
	AuthResp login(AuthRequest dto);
}

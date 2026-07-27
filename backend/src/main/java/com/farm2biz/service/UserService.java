package com.farm2biz.service;


import java.util.List;

import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.UserDTO;

public interface UserService {

	UserDTO createUser(UserDTO dto);
	UserDTO getUserById(Long userId);
	List getAllUsers();
	UserDTO updateUser(Long userId, UserDTO dto);
	ApiResponse deleteUser(Long userId);
}

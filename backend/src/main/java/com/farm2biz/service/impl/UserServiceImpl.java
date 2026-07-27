package com.farm2biz.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.UserDTO;
import com.farm2biz.entities.User;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.service.UserService;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	// ModelMapper created directly
	// entity<->DTO conversion just makes its own instance like this.
	private ModelMapper mapper = new ModelMapper();

	@Override
	public UserDTO createUser(UserDTO dto) {
		
		User user = mapper.map(dto, User.class);
		User saved = userRepository.save(user);
		return mapper.map(saved, UserDTO.class);
	}

	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + userId));
		return mapper.map(user, UserDTO.class);
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(user -> mapper.map(user, UserDTO.class))
				.toList();
	}

	@Override
	public UserDTO updateUser(Long userId, UserDTO dto) {
		User existing = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + userId));

		existing.setName(dto.getName());
		existing.setEmail(dto.getEmail());
		existing.setPassword(dto.getPassword());
		existing.setRole(dto.getRole());

		User updated = userRepository.save(existing);
		return mapper.map(updated, UserDTO.class);
	}

	@Override
	public ApiResponse deleteUser(Long userId) {
		User existing = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + userId));

		userRepository.delete(existing);
		return new ApiResponse("User deleted successfully", "success");
	}
	
}



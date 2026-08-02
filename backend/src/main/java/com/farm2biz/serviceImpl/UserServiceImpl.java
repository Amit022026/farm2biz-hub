package com.farm2biz.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.AuthRequest;
import com.farm2biz.dtos.AuthResp;
import com.farm2biz.dtos.UserDTO;
import com.farm2biz.entities.User;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.security.JwtUtil;
import com.farm2biz.service.UserService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	private ModelMapper mapper = new ModelMapper();

	@Override
	public UserDTO createUser(UserDTO dto) {

		User user = mapper.map(dto, User.class);
		// Never store plain-text passwords - always hash
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		User saved = userRepository.save(user);
		
		UserDTO response = mapper.map(saved, UserDTO.class);
		response.setPassword(null); // never return any form of the password
		return response;
	}
	@Override
	public AuthResp login(AuthRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

		String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

		return new AuthResp(token, user.getUserId(), user.getName(), user.getEmail(), user.getRole().name());
	}
	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + userId));
		UserDTO dto = mapper.map(user, UserDTO.class);
		dto.setPassword(null);
		return dto;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(user -> {
					UserDTO dto = mapper.map(user, UserDTO.class);
					dto.setPassword(null);
					return dto;
				})
				.toList();
	}

	@Override
	public UserDTO updateUser(Long userId, UserDTO dto) {
		User existing = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"User not found with id: " + userId));

		existing.setName(dto.getName());
		existing.setEmail(dto.getEmail());
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			existing.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		existing.setRole(dto.getRole());

		User updated = userRepository.save(existing);
		UserDTO response = mapper.map(updated, UserDTO.class);
		response.setPassword(null);
		return response;
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



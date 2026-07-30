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
	
	// NEW in Phase 2 - three more things this Service now depends on:
		private final PasswordEncoder passwordEncoder;       // hashes passwords before saving
		private final AuthenticationManager authenticationManager; // checks email+password during login
		private final JwtUtil jwtUtil;                        // issues the token after a successful login

	// ModelMapper created directly
	// entity<->DTO conversion just makes its own instance like this.
	private ModelMapper mapper = new ModelMapper();

	@Override
	public UserDTO createUser(UserDTO dto) {

		User user = mapper.map(dto, User.class);
		// CRITICAL: never save a plain-text password. BCrypt turns
				// "test123" into something like "$2a$10$N9qo8uLOickgx2ZMRZoMy..."
				// - a one-way hash. Even if our database were ever leaked, nobody
				// could recover the original password from this string.
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		User saved = userRepository.save(user);
		
		UserDTO response = mapper.map(saved, UserDTO.class);
		response.setPassword(null); // NEVER send any form of the password back, hashed or not
		return response;
	}
	@Override
	public AuthResp login(AuthRequest request) {
		// This one line does A LOT: it looks up the user via
		// CustomUserDetailsService, then uses PasswordEncoder to check if
		// the submitted password's hash matches the stored hash. If
		// anything is wrong (user doesn't exist, wrong password), it
		// throws BadCredentialsException automatically - we don't have to
		// write that comparison logic ourselves.
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		// If we reach this line, the password was CORRECT. Now fetch the
		// full user record so we can build our response.
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



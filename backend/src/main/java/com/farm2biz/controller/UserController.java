package com.farm2biz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farm2biz.dtos.AuthRequest;
import com.farm2biz.dtos.UserDTO;
import com.farm2biz.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

    @RestController
	@RequestMapping("/users")
	@RequiredArgsConstructor
	public class UserController {

		private final UserService userService;

		/*
		 * Desc  - Register a new user (Farmer or Bulk Buyer sign-up)
		 * URI   - http://host:port/users/register
		 * Method- POST
		 * Payload - request body - UserDTO (name, email, password, role)
		 * Success Response - SC 201 + UserDTO (password NEVER included in response)
		 * Error Response   - SC 400 + validation error details
		 * Access - PUBLIC (no token required)
		 */
		@PostMapping("/register")
		public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO dto) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
		}
		/*
		 * Desc  - Log in with email + password, receive a JWT token
		 * URI   - http://host:port/users/login
		 * Method- POST
		 * Payload - request body - AuthRequest (email, password)
		 * Success Response - SC 200 + AuthResp (token, userId, name, email, role)
		 * Error Response   - SC 401 if email/password is wrong (see GlobalExceptionHandler)
		 * Access - PUBLIC (no token required - this IS how you get one)
		 */
		@PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
			return ResponseEntity.ok(userService.login(request));
		}

		/*
		 * Desc  - Get one user's details by id
		 * URI   - http://host:port/users/{userId}
		 * Method- GET
		 * Access - any LOGGED-IN user (any role)
		 */
		@GetMapping("/{userId}")
		public ResponseEntity<?> getUserById(@PathVariable Long userId) {
			return ResponseEntity.ok(userService.getUserById(userId));
		}
		/*
		 * Desc  - List every user - "Admin -> Manage Users" from the use case diagram
		 * URI   - http://host:port/users
		 * Method- GET
		 * Access - ADMIN only
		 */
		@GetMapping
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> getAllUsers() {
			List<UserDTO> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
		}

		/*
		 * Desc  - Update a user's details - "Admin -> Manage Users"
		 * URI   - http://host:port/users/{userId}
		 * Method- PUT
		 * Access - ADMIN only
		 */
		@PutMapping("/{userId}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO dto) {
			return ResponseEntity.ok(userService.updateUser(userId, dto));
		}

		/*
		 * Desc  - Delete a user - "Admin -> Manage Users"
		 * URI   - http://host:port/users/{userId}
		 * Method- DELETE
		 * Access - ADMIN only
		 */
		@DeleteMapping("/{userId}")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
			return ResponseEntity.ok(userService.deleteUser(userId));
		}
}

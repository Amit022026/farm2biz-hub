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

		// password is never serialized in the response
		@PostMapping("/register")
		public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO dto) {
			return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
		}
		@PostMapping("/login")
		public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
			return ResponseEntity.ok(userService.login(request));
		}

		@GetMapping("/{userId}")
		public ResponseEntity<?> getUserById(@PathVariable Long userId) {
			return ResponseEntity.ok(userService.getUserById(userId));
		}
		@GetMapping
		public ResponseEntity<?> getAllUsers() {
			List<UserDTO> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
		}

		@PutMapping("/{userId}")
		public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO dto) {
			return ResponseEntity.ok(userService.updateUser(userId, dto));
		}

		@DeleteMapping("/{userId}")
		public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
			return ResponseEntity.ok(userService.deleteUser(userId));
		}
}

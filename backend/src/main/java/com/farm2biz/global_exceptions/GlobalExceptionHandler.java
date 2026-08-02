package com.farm2biz.global_exceptions;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.farm2biz.custom_exceptions.InvalidInputException;
import com.farm2biz.custom_exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.NOT_FOUND.value());
			body.put("message", ex.getMessage());
			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		}

		// Deliberately vague: never reveal which credential was wrong
		@ExceptionHandler(BadCredentialsException.class)
		public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.UNAUTHORIZED.value());
			body.put("message", "Invalid email or password");
			return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
		}

		@ExceptionHandler(AccessDeniedException.class)
		public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.FORBIDDEN.value());
			body.put("message", "You do not have permission to perform this action");
			return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
		}

		@ExceptionHandler(InvalidInputException.class)
		public ResponseEntity<Map<String, Object>> handleInvalidInput(InvalidInputException ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.BAD_REQUEST.value());
			body.put("message", ex.getMessage());
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}

		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
			Map<String, String> fieldErrors = new HashMap<>();
			for (FieldError error : ex.getBindingResult().getFieldErrors()) {
				fieldErrors.put(error.getField(), error.getDefaultMessage());
			}

			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.BAD_REQUEST.value());
			body.put("message", "Validation failed");
			body.put("errors", fieldErrors);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}

		@ExceptionHandler(Exception.class)
		public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			body.put("message", "Something went wrong: " + ex.getMessage());
			return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}

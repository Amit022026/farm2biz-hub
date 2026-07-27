package com.farm2biz.global_exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.NOT_FOUND.value()); // 404
			body.put("message", ex.getMessage());
			return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		}

		// Catches validation failures - triggered automatically whenever a
		// @Valid @RequestBody DTO fails one of its constraints (@NotBlank,
		// @DecimalMin, etc. - see ProductDTO / UserDTO). Without this handler,
		// Spring's default 400 response is much harder to read.
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
			Map<String, String> fieldErrors = new HashMap<>();
			for (FieldError error : ex.getBindingResult().getFieldErrors()) {
				fieldErrors.put(error.getField(), error.getDefaultMessage());
			}

			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.BAD_REQUEST.value()); // 400
			body.put("message", "Validation failed");
			body.put("errors", fieldErrors);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}

		// Catch-all safety net: anything we didn't specifically plan for still
		// comes back as clean JSON with a 500, instead of leaking a raw Java
		// stack trace to whoever called the API.
		@ExceptionHandler(Exception.class)
		public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500
			body.put("message", "Something went wrong: " + ex.getMessage());
			return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}

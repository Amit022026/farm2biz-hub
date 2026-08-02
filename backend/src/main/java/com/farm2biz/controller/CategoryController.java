package com.farm2biz.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farm2biz.dtos.CategoryDTO;
import com.farm2biz.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// authorization rules centralized in SecurityConfig (no @PreAuthorize here)
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
	}
	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
	}
	@GetMapping
	public ResponseEntity<?> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}
	@PutMapping("/{categoryId}")
	public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryDTO dto) {
		return ResponseEntity.ok(categoryService.updateCategory(categoryId, dto));
	}
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
	}
		
}

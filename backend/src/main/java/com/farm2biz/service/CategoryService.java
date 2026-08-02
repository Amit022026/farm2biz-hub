package com.farm2biz.service;

import java.util.List;

import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.CategoryDTO;

import jakarta.validation.Valid;

public interface CategoryService {

	CategoryDTO createCategory(@Valid CategoryDTO dto);

	CategoryDTO getCategoryById(Long categoryId);

	List<CategoryDTO> getAllCategories();

	CategoryDTO updateCategory(Long categoryId, @Valid CategoryDTO dto);

	ApiResponse deleteCategory(Long categoryId);

}

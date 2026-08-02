package com.farm2biz.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.CategoryDTO;
import com.farm2biz.entities.Category;
import com.farm2biz.repository.CategoryRepository;
import com.farm2biz.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private ModelMapper mapper = new ModelMapper();

	@Override
	public CategoryDTO createCategory(CategoryDTO dto) {
		Category category = mapper.map(dto, Category.class);
		Category saved = categoryRepository.save(category);
		return mapper.map(saved, CategoryDTO.class);
	}

	@Override
	public CategoryDTO getCategoryById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
		return mapper.map(category, CategoryDTO.class);
	}

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll().stream().map(category -> mapper.map(category, CategoryDTO.class)).toList();
	}

	@Override
	public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto) {
		Category existing = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

		existing.setName(dto.getName());
		existing.setDescription(dto.getDescription());

		Category updated = categoryRepository.save(existing);
		return mapper.map(updated, CategoryDTO.class);
	}

	@Override
	public ApiResponse deleteCategory(Long categoryId) {
		Category existing = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
		categoryRepository.delete(existing);
		return new ApiResponse("Category deleted successfully", "success");
	}

}

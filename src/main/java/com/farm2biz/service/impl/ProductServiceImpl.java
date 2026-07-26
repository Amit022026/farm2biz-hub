package com.farm2biz.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.ProductDTO;
import com.farm2biz.entities.Product;
import com.farm2biz.repository.ProductRepository;
import com.farm2biz.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public ProductDTO createProduct(ProductDTO dto) {

		Product product = new Product();
		product.setName(dto.getName());
		product.setCategory(dto.getCategory());
		product.setPrice(dto.getPrice());
		product.setQuantityAvailable(dto.getQuantityAvailable());
		product.setUnit(dto.getUnit());

		Product saved = productRepository.save(product); // INSERT happens here
		return mapToDto(saved);
	}

	@Override
	public ProductDTO getProductById(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
		return mapToDto(product);
	}

	@Override
	public List<ProductDTO> getAllProducts() {
		return productRepository.findAll().stream().map(this::mapToDto).toList();
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO dto) {
		Product existing = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		existing.setName(dto.getName());
		existing.setCategory(dto.getCategory());
		existing.setPrice(dto.getPrice());
		existing.setQuantityAvailable(dto.getQuantityAvailable());
		existing.setUnit(dto.getUnit());

		Product updated = productRepository.save(existing); // UPDATE happens here
		return mapToDto(updated);
	}

	@Override
	public ApiResponse deleteProduct(Long productId) {
		Product existing = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		productRepository.delete(existing); // DELETE happens here
		return new ApiResponse("Product deleted successfully", "success");
	}
//mapping
	private ProductDTO mapToDto(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setProductId(product.getProductId());
		dto.setName(product.getName());
		dto.setCategory(product.getCategory());
		dto.setPrice(product.getPrice());
		dto.setQuantityAvailable(product.getQuantityAvailable());
		dto.setUnit(product.getUnit());
		return dto;
	}
}

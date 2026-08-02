package com.farm2biz.serviceImpl;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.ProductDTO;
import com.farm2biz.entities.Category;
import com.farm2biz.entities.Product;
import com.farm2biz.entities.User;
import com.farm2biz.repository.CategoryRepository;
import com.farm2biz.repository.ProductRepository;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.service.ProductService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService{

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;

	   private ModelMapper mapper = new ModelMapper();
	
	   @Override
		public ProductDTO createProduct(ProductDTO dto) {
			User farmer = userRepository.findById(dto.getFarmerId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Farmer not found with id: " + dto.getFarmerId()));

			Category category = categoryRepository.findById(dto.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Category not found with id: " + dto.getCategoryId()));

			Product product = mapper.map(dto, Product.class);
			product.setFarmer(farmer);
			product.setCategory(category);

			Product saved = productRepository.save(product);
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
		return productRepository.findAll()
				.stream()
				.map(this::mapToDto)
				.toList();
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO dto) {
		Product existing = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		existing.setName(dto.getName());
		existing.setPrice(dto.getPrice());
		existing.setQuantityAvailable(dto.getQuantityAvailable());
		existing.setUnit(dto.getUnit());
			// Category is mutable on update; farmer (ownership) is not
				if (dto.getCategoryId() != null) {
					Category category = categoryRepository.findById(dto.getCategoryId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Category not found with id: " + dto.getCategoryId()));
					existing.setCategory(category);
				}

				Product updated = productRepository.save(existing);
				return mapToDto(updated);
	}

	@Override
	public ApiResponse deleteProduct(Long productId) {
		Product existing = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

		productRepository.delete(existing);
		return new ApiResponse("Product deleted successfully", "success");
	}
	
		private ProductDTO mapToDto(Product product) {
			ProductDTO dto = mapper.map(product, ProductDTO.class);
			if (product.getFarmer() != null) {
				dto.setFarmerId(product.getFarmer().getUserId());
				dto.setFarmerName(product.getFarmer().getName());
			}
			if (product.getCategory() != null) {
				dto.setCategoryId(product.getCategory().getCategoryId());
				dto.setCategoryName(product.getCategory().getName());
			}
			return dto;
		}
}

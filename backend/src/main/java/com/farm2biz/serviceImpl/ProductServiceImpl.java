package com.farm2biz.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.ProductDTO;
import com.farm2biz.entities.Product;
import com.farm2biz.entities.User;
import com.farm2biz.repository.ProductRepository;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.service.ProductService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
 // Needed now because creating a Product means looking up an existing
 	// farmer by the farmerId the client sent us - Product can't be saved
 	// without a real, existing User attached to it.
	private final UserRepository userRepository;

	   private ModelMapper mapper = new ModelMapper();
	
	   @Override
		public ProductDTO createProduct(ProductDTO dto) {
			// Step 1: find the farmer this product belongs to. If farmerId
			// doesn't match any real User row, we stop right here with a
			// clear error instead of saving a "broken" product with no owner.
			User farmer = userRepository.findById(dto.getFarmerId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Farmer not found with id: " + dto.getFarmerId()));

			// Step 2: convert the simple fields (name, price, ...) automatically
			Product product = mapper.map(dto, Product.class);

			// Step 3: ModelMapper cannot know how to turn a Long farmerId into
			// a full User object by itself - that relationship lookup is OUR
			// business logic, so we set it manually after the auto-mapping.
			product.setFarmer(farmer);

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
		existing.setCategory(dto.getCategory());
		existing.setPrice(dto.getPrice());
		existing.setQuantityAvailable(dto.getQuantityAvailable());
		existing.setUnit(dto.getUnit());
		// NOTE: we deliberately do NOT let an update change the farmer -
				// ownership of a listing shouldn't silently transfer via a PUT.
				// (If that's ever needed, it should be its own explicit endpoint.)
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
	// Why this method is NOT just "modelMapper.map(product, ProductDTO.class)":
		// Product.farmer is a whole User object, but ProductDTO only wants
		// farmerId (Long) and farmerName (String) - two flat fields pulled OUT
		// of that nested object. ModelMapper's automatic matching can get this
		// wrong or throw on lazy-loaded relationships, so for this one
		// direction (entity -> dto) we map the simple fields automatically,
		// then set the two farmer-derived fields by hand.
	private ProductDTO mapToDto(Product product) {
		ProductDTO dto = mapper.map(product, ProductDTO.class);
		if (product.getFarmer() != null) {
			dto.setFarmerId(product.getFarmer().getUserId());
			dto.setFarmerName(product.getFarmer().getName());
		}
		return dto;
	}
}

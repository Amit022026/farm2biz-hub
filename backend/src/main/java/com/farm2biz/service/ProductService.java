package com.farm2biz.service;

import java.util.List;

import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.ProductDTO;

public interface ProductService {
	ProductDTO createProduct(ProductDTO dto);

	ProductDTO getProductById(Long productId);

	List<ProductDTO> getAllProducts();

	ProductDTO updateProduct(Long productId, ProductDTO dto);

	ApiResponse deleteProduct(Long productId);
}

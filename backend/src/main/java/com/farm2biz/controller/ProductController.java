package com.farm2biz.controller;

import java.util.List;

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

import com.farm2biz.dtos.ProductDTO;
import com.farm2biz.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	// dependency - injected by Spring automatically (see ProductServiceImpl)
	private final ProductService productService;

	/*
	 * Desc  - Create a new product listing - "Farmer -> Add Product"
	 * URI   - http://host:port/products
	 * Method- POST
	 * Payload - request body - ProductDTO (name, category, price, quantityAvailable, unit, farmerId)
	 * Success Response - SC 201 + ProductDTO (now includes generated productId)
	 * Error Response   - SC 400 (validation) / SC 401 (not logged in) / SC 403 (logged in, wrong role)
	 * Access - FARMER only
	 */
	@PostMapping
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO dto) {
		System.out.println("in create product " + dto);
		ProductDTO created = productService.createProduct(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	/*
	 * Desc  - Get one product's details by its id - "Buyer -> Search Product"
	 * URI   - http://host:port/products/{productId}
	 * Method- GET
	 * Access - PUBLIC (see SecurityConfig: GET /products/** is permitAll)
	 */
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Long productId) {
		System.out.println("in get product by id " + productId);
		return ResponseEntity.ok(productService.getProductById(productId));
	}
	/*
	 * Desc  - List every product - "Buyer -> Search Product"
	 * URI   - http://host:port/products
	 * Method- GET
	 * Access - PUBLIC
	 */
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		System.out.println("in get all products");
		List<ProductDTO> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}

	/*
	 * Desc  - Update an existing product's details - "Farmer -> Edit Product"
	 * URI   - http://host:port/products/{productId}
	 * Method- PUT
	 * Access - FARMER only
	 */
	@PutMapping("/{productId}")
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductDTO dto) {
		System.out.println("in update product " + productId + " " + dto);
		return ResponseEntity.ok(productService.updateProduct(productId, dto));
	}

	/*
	 * Desc  - Delete a product listing - "Farmer -> Delete Product"
	 * URI   - http://host:port/products/{productId}
	 * Method- DELETE
	 * Access - FARMER only
	 */
	@DeleteMapping("/{productId}")
	@PreAuthorize("hasRole('FARMER')")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
		System.out.println("in delete product " + productId);
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}
}

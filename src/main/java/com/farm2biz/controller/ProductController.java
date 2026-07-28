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

import com.farm2biz.dtos.ProductDTO;
import com.farm2biz.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
//add product
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO dto) {
		System.out.println("in create product " + dto);
		ProductDTO created = productService.createProduct(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
//get product by Id
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Long productId) {
		System.out.println("in get product by id " + productId);
		return ResponseEntity.ok(productService.getProductById(productId));
	}
//get all products
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		System.out.println("in get all products");
		List<ProductDTO> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}
//update products
	@PutMapping("/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductDTO dto) {
		System.out.println("in update product " + productId + " " + dto);
		return ResponseEntity.ok(productService.updateProduct(productId, dto));
	}
//delete product
	@DeleteMapping("/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
		System.out.println("in delete product " + productId);
		return ResponseEntity.ok(productService.deleteProduct(productId));
	}
}

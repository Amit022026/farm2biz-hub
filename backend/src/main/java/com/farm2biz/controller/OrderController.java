package com.farm2biz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.farm2biz.dtos.OrderRequest;
import com.farm2biz.security.CustomUserDetailsImpl;
import com.farm2biz.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	// buyer identity comes from the token, never from the request body
	@PostMapping
	public ResponseEntity<?> placeOrder(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@RequestBody @Valid OrderRequest request) {
		Long buyerId = principal.getUser().getUserId();
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(buyerId, request));
	}
	// only the farmer who owns the product may accept an order
	@PatchMapping("/{orderId}/accept")
	public ResponseEntity<?> acceptOrder(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@PathVariable Long orderId) {
		Long farmerId = principal.getUser().getUserId();
		return ResponseEntity.ok(orderService.acceptOrder(orderId, farmerId));
	}
	@PatchMapping("/{orderId}/reject")
	public ResponseEntity<?> rejectOrder(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@PathVariable Long orderId) {
		Long farmerId = principal.getUser().getUserId();
		return ResponseEntity.ok(orderService.rejectOrder(orderId, farmerId));
	}
	// only the order's own buyer may cancel; stock is restored automatically
	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@PathVariable Long orderId) {
		Long buyerId = principal.getUser().getUserId();
		return ResponseEntity.ok(orderService.cancelOrder(orderId, buyerId));
	}
	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
		return ResponseEntity.ok(orderService.getOrderById(orderId));
	}
	@GetMapping("/my-orders")
	public ResponseEntity<?> getMyOrders(@AuthenticationPrincipal CustomUserDetailsImpl principal) {
		Long buyerId = principal.getUser().getUserId();
		return ResponseEntity.ok(orderService.getMyOrders(buyerId));
	}
	
	@GetMapping("/farmer-orders")
	public ResponseEntity<?> getFarmerOrders(@AuthenticationPrincipal CustomUserDetailsImpl principal) {
		Long farmerId = principal.getUser().getUserId();
		return ResponseEntity.ok(orderService.getFarmerOrders(farmerId));
	}
	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		List<?> orders = orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	}
	
	
}

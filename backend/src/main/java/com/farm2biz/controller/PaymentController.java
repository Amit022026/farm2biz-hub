package com.farm2biz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farm2biz.dtos.PaymentRequest;
import com.farm2biz.security.CustomUserDetailsImpl;
import com.farm2biz.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	/*
	 * Desc  - Pay for an order (simulated gateway)
	 * Access - BULK_BUYER only, AND must be the order's own buyer (enforced in Service)
	 */
	@PostMapping
	public ResponseEntity<?> makePayment(@AuthenticationPrincipal CustomUserDetailsImpl principal,
			@RequestBody @Valid PaymentRequest request) {
		Long buyerId = principal.getUser().getUserId();
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.makePayment(buyerId, request));
	}
	/*
	 * Desc  - Check the payment status for a given order
	 * URI   - http://host:port/payments/order/{orderId}
	 * Access - any authenticated user
	 */
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<?> getPaymentForOrder(@PathVariable Long orderId) {
		return ResponseEntity.ok(paymentService.getPaymentForOrder(orderId));
	}
	
}

package com.farm2biz.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderResponse {
	private Long orderId;

	private Long buyerId;
	private String buyerName;

	private Long productId;
	private String productName;

	private Long farmerId;   // pulled from product.farmer
	private String farmerName;

	private BigDecimal quantity;
	private BigDecimal totalAmount;
	private String status; // plain text enum, e.g. "PENDING"

	private String deliveryAddress;
	
	private LocalDateTime orderDate;
}

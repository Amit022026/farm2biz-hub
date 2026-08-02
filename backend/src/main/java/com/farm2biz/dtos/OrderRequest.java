package com.farm2biz.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// no buyerId - buyer identity comes from the JWT, never from client input

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {

	@NotNull(message = "productId is required")
	private Long productId;

	@NotNull(message = "Quantity is required")
	@DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
	private BigDecimal quantity;

	@NotBlank(message = "Delivery address is required")
	private String deliveryAddress;
}

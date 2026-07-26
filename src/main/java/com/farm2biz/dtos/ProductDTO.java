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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

	private Long productId; // ignored on create, populated on response

	@NotBlank(message = "Product name is required")
	private String name;

	private String category;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;

	@NotNull(message = "Quantity available is required")
	@DecimalMin(value = "0.0", message = "Quantity cannot be negative")
	private BigDecimal quantityAvailable;

	private String unit;
}

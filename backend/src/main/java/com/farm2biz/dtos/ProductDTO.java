package com.farm2biz.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Data
public class ProductDTO {

	private Long productId; // ignored on create, populated on response

	@NotBlank(message = "Product name is required")
	private String name;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;

	@NotNull(message = "Quantity available is required")
	@DecimalMin(value = "0.0", message = "Quantity cannot be negative")
	private BigDecimal quantityAvailable;

	private String unit;
	

	@NotNull(message = "farmerId is required")
	private Long farmerId; // client sends on create

	private String farmerName;

	@NotNull(message = "categoryId is required")
	private Long categoryId; // client sends on create

	private String categoryName;
	
}

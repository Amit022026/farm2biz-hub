package com.farm2biz.dtos;

import com.farm2biz.entities.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Same design rule as OrderRequest: no "buyerId" field here. We NEVER
//trust the client to say who they are - that always comes from the JWT
//token via @AuthenticationPrincipal in the Controller.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRequest {
	@NotNull(message = "orderId is required")
	private Long orderId;

	@NotNull(message = "Payment method is required")
	private PaymentMethod method;
}

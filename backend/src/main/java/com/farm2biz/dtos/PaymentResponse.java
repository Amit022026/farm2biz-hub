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
public class PaymentResponse {
	private Long paymentId;
	private Long orderId;
	private BigDecimal amount;
	private String method;  // e.g. "UPI"
	private String status;  // e.g. "SUCCESS"
	private String transactionRef;
	private LocalDateTime paidAt;
}

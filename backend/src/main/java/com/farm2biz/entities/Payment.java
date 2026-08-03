package com.farm2biz.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//NEW relationship @OneToOne.exactly ONE Order can have AT
//MOST one Payment, and one Payment belongs to exactly ONE Order - a
//strict 1-to-1 pairing.
//
//@Table(name = "payments") - explicitly named on purpose  After the "order" reserved-keyword incident, the

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	// unique = true is what actually ENFORCES the "one-to-one" rule at the
	// database level - MySQL will physically refuse to let two different
	// Payment rows point at the same order_id. Without this, nothing would
	// stop two payments from accidentally being created for one order.
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", unique = true)
	private Order order;

	private BigDecimal amount; // copied from order.totalAmount -> see PaymentServiceImpl

	@Enumerated(EnumType.STRING)
	private PaymentMethod method;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private String transactionRef; // a fake/simulated gateway reference id - see PaymentServiceImpl

	private LocalDateTime paidAt;

	private LocalDateTime createdAt;
}

package com.farm2biz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

	// Follows the "order" relationship, then filters by its orderId -
		// same underscore-navigation pattern as OrderRepository's
		// findByBuyer_UserId. Returns Optional because a given order might
		// have NO payment yet at all.
		Optional<Payment> findByOrder_OrderId(Long orderId);
}

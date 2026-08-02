package com.farm2biz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farm2biz.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	// '_' separates the relationship path: Order -> buyer -> userId
		List<Order> findByBuyer_UserId(Long buyerId);

		List<Order> findByProduct_Farmer_UserId(Long farmerId);

}

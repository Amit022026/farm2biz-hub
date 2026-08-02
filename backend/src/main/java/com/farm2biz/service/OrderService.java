package com.farm2biz.service;

import java.util.List;

import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.OrderRequest;
import com.farm2biz.dtos.OrderResponse;

import jakarta.validation.Valid;

public interface OrderService {
	OrderResponse placeOrder(Long buyerId, @Valid OrderRequest request);

	OrderResponse acceptOrder(Long orderId, Long farmerId);

	OrderResponse rejectOrder(Long orderId, Long farmerId);

	ApiResponse cancelOrder(Long orderId, Long buyerId);

	OrderResponse getOrderById(Long orderId);

	List<OrderResponse> getMyOrders(Long buyerId);

	List<OrderResponse> getFarmerOrders(Long farmerId);

	List<OrderResponse> getAllOrders();

}

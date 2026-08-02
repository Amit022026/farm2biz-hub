package com.farm2biz.serviceImpl;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.InvalidInputException;
import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.ApiResponse;
import com.farm2biz.dtos.OrderRequest;
import com.farm2biz.dtos.OrderResponse;
import com.farm2biz.entities.Order;
import com.farm2biz.entities.OrderStatus;
import com.farm2biz.entities.Product;
import com.farm2biz.entities.User;
import com.farm2biz.repository.OrderRepository;
import com.farm2biz.repository.ProductRepository;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public OrderResponse placeOrder(Long buyerId, OrderRequest request) {
		User buyer = userRepository.findById(buyerId)
				.orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: " + buyerId));

		Product product = productRepository.findById(request.getProductId()).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

		// Cannot order more than available stock; compare BigDecimal with compareTo, never == or < >
		if (product.getQuantityAvailable().compareTo(request.getQuantity()) < 0) {
			throw new InvalidInputException("Insufficient stock for '" + product.getName() + "'. Available: "
					+ product.getQuantityAvailable() + " " + product.getUnit());
		}

		// Reserve stock at order placement to prevent overselling
		product.setQuantityAvailable(product.getQuantityAvailable().subtract(request.getQuantity()));
		productRepository.save(product);

		BigDecimal total = product.getPrice().multiply(request.getQuantity());

		Order order = new Order();
		order.setBuyer(buyer);
		order.setProduct(product);
		order.setQuantity(request.getQuantity());
		order.setTotalAmount(total);
		order.setDeliveryAddress(request.getDeliveryAddress());
		order.setStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDateTime.now());

		Order saved = orderRepository.save(order);
		return toResponse(saved);
	}

	@Override
	@Transactional
	public OrderResponse acceptOrder(Long orderId, Long farmerId) {
		Order order = getOrderOrThrow(orderId);

		assertFarmerOwnsOrder(order, farmerId);

		assertStatus(order, OrderStatus.PENDING, "accept");

		order.setStatus(OrderStatus.ACCEPTED);
		return toResponse(orderRepository.save(order));
	}

	
	@Override
	@Transactional
	public OrderResponse rejectOrder(Long orderId, Long farmerId) {
		Order order = getOrderOrThrow(orderId);
		assertFarmerOwnsOrder(order, farmerId);
		assertStatus(order, OrderStatus.PENDING, "reject");

		// Rejected orders are never fulfilled - give reserved stock back
		restockProduct(order);

		order.setStatus(OrderStatus.REJECTED);
		return toResponse(orderRepository.save(order));
	}
	
	
	@Override
	@Transactional
	public ApiResponse cancelOrder(Long orderId, Long buyerId) {
		Order order = getOrderOrThrow(orderId);

		if (!order.getBuyer().getUserId().equals(buyerId)) {
			throw new InvalidInputException("You can only cancel your own orders");
		}

		assertStatus(order, OrderStatus.PENDING, "cancel");
		restockProduct(order);

		order.setStatus(OrderStatus.CANCELLED);
		orderRepository.save(order);
		return new ApiResponse("Order cancelled successfully", "success");
	}

	@Override
	public OrderResponse getOrderById(Long orderId) {
		return toResponse(getOrderOrThrow(orderId));
	}

	@Override
	public List<OrderResponse> getMyOrders(Long buyerId) {
		return orderRepository.findByBuyer_UserId(buyerId).stream().map(this::toResponse).toList();
	}

	@Override
	public List<OrderResponse> getFarmerOrders(Long farmerId) {
		return orderRepository.findByProduct_Farmer_UserId(farmerId).stream().map(this::toResponse).toList();
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll().stream().map(this::toResponse).toList();
	}

	private Order getOrderOrThrow(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
	}

	private void assertFarmerOwnsOrder(Order order, Long farmerId) {
		Long actualFarmerId = order.getProduct().getFarmer().getUserId();
		if (!actualFarmerId.equals(farmerId)) {
			throw new InvalidInputException("You do not own the product in this order");
		}
	}

	private void assertStatus(Order order, OrderStatus expected, String action) {
		if (order.getStatus() != expected) {
			throw new InvalidInputException("Cannot " + action + " an order that is currently " + order.getStatus());
		}
	}

	private void restockProduct(Order order) {
		Product product = order.getProduct();
		product.setQuantityAvailable(product.getQuantityAvailable().add(order.getQuantity()));
		productRepository.save(product);
	}

	private OrderResponse toResponse(Order order) {
		OrderResponse dto = new OrderResponse();
		dto.setOrderId(order.getOrderId());
		dto.setBuyerId(order.getBuyer().getUserId());
		dto.setBuyerName(order.getBuyer().getName());
		dto.setProductId(order.getProduct().getProductId());
		dto.setProductName(order.getProduct().getName());
		dto.setFarmerId(order.getProduct().getFarmer().getUserId());
		dto.setFarmerName(order.getProduct().getFarmer().getName());
		dto.setQuantity(order.getQuantity());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setStatus(order.getStatus().name());
		dto.setDeliveryAddress(order.getDeliveryAddress());
		dto.setOrderDate(order.getOrderDate());
		return dto;
	}

}

package com.farm2biz.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.farm2biz.entities.Order;
import com.farm2biz.entities.OrderStatus;
import com.farm2biz.entities.Role;
import com.farm2biz.dtos.ReportSummaryDTO;
import com.farm2biz.repository.CategoryRepository;
import com.farm2biz.repository.OrderRepository;
import com.farm2biz.repository.ProductRepository;
import com.farm2biz.repository.UserRepository;
import com.farm2biz.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	// This Service is unusual it depends on FOUR different repositories at once.
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final OrderRepository orderRepository;

	public ReportSummaryDTO getSummary() {
		ReportSummaryDTO summary = new ReportSummaryDTO();

		// .count() is another free method JpaRepository gives every
		// repository - runs "SELECT COUNT(*) FROM ..." for us.
		long totalFarmers = userRepository.findAll().stream()
				.filter(u -> u.getRole() == Role.FARMER)
				.count();
		long totalBulkBuyers = userRepository.findAll().stream()
				.filter(u -> u.getRole() == Role.BULK_BUYER)
				.count();

		summary.setTotalFarmers(totalFarmers);
		summary.setTotalBulkBuyers(totalBulkBuyers);
		summary.setTotalProducts(productRepository.count());
		summary.setTotalCategories(categoryRepository.count());

		List<Order> allOrders = orderRepository.findAll();
		summary.setTotalOrders(allOrders.size());

		// Group all orders by their status and count each group - answers
		// "how many orders are PENDING right now vs ACCEPTED vs REJECTED
		// vs CANCELLED" in one pass over the data.
		Map<String, Long> ordersByStatus = allOrders.stream()
				.collect(Collectors.groupingBy(
						order -> order.getStatus().name(),
						Collectors.counting()));
		summary.setOrdersByStatus(ordersByStatus);

		// BUSINESS DECISION: "revenue" only counts orders the farmer
		// actually ACCEPTED. A PENDING order might still be rejected or
		// cancelled, and a REJECTED/CANCELLED order was never fulfilled -
		// counting those as revenue would overstate the real number.
		BigDecimal totalRevenue = allOrders.stream()
				.filter(order -> order.getStatus() == OrderStatus.ACCEPTED)
				.map(Order::getTotalAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		summary.setTotalRevenue(totalRevenue);

		return summary;
	}
}


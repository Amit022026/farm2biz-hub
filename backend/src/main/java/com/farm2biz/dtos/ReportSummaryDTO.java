package com.farm2biz.dtos;

import java.math.BigDecimal;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// report figures are computed on demand from other tables - never persisted
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportSummaryDTO {
	private long totalFarmers;
	private long totalBulkBuyers;
	private long totalProducts;
	private long totalCategories;
	private long totalOrders;
	private Map<String, Long> ordersByStatus;
	private BigDecimal totalRevenue; // sum of totalAmount for ACCEPTED orders only	
}


package com.farm2biz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farm2biz.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
	private final ReportService reportService;
	/*
	 * Get a platform-wide summary - "Admin -> View Reports"
	 * http://host:port/reports/summary
	 * ADMIN only
	 */
	@GetMapping("/summary")
	public ResponseEntity<?> getSummary() {
		return ResponseEntity.ok(reportService.getSummary());
	}
}

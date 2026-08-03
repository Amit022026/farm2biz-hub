package com.farm2biz.service;

import com.farm2biz.dtos.PaymentRequest;
import com.farm2biz.dtos.PaymentResponse;

public interface PaymentService {
	PaymentResponse makePayment(Long buyerId, PaymentRequest request);
	PaymentResponse getPaymentForOrder(Long orderId);
}

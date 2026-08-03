package com.farm2biz.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.farm2biz.custom_exceptions.InvalidInputException;
import com.farm2biz.custom_exceptions.ResourceNotFoundException;
import com.farm2biz.dtos.PaymentRequest;
import com.farm2biz.dtos.PaymentResponse;
import com.farm2biz.entities.Order;
import com.farm2biz.entities.OrderStatus;
import com.farm2biz.entities.Payment;
import com.farm2biz.entities.PaymentStatus;
import com.farm2biz.repository.OrderRepository;
import com.farm2biz.repository.PaymentRepository;
import com.farm2biz.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//NOTE (learned the hard way from the Order bugs above): this method IS
//@Transactional from the very start this time, not added later as a
//patch - a Payment write is exactly the kind of "must not half-happen"
//operation @Transactional exists for.
// processGateway()below SIMULATES that entire flow with a method that just always
//returns true
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	
		@Override
		@Transactional
		public PaymentResponse makePayment(Long buyerId, PaymentRequest request) {
			Order order = orderRepository.findById(request.getOrderId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Order not found with id: " + request.getOrderId()));

			// OWNERSHIP CHECK - same pattern as Order's cancel: you can only
			// pay for YOUR OWN order.
			if (!order.getBuyer().getUserId().equals(buyerId)) {
				throw new InvalidInputException("You can only pay for your own orders");
			}

			// BUSINESS RULE: a farmer must have ACCEPTED the order before
			// payment is collected - paying for an order that might still be
			// rejected doesn't make sense in this workflow.
			if (order.getStatus() != OrderStatus.ACCEPTED) {
				throw new InvalidInputException(
						"Cannot pay for an order that is currently " + order.getStatus()
								+ " - only ACCEPTED orders can be paid for");
			}

			// BUSINESS RULE: block paying twice for the same order.
			paymentRepository.findByOrder_OrderId(order.getOrderId()).ifPresent(existing -> {
				throw new InvalidInputException("This order has already been paid for");
			});

			boolean success = processGateway(); // simulated - see class comment above

			Payment payment = new Payment();
			payment.setOrder(order);
			payment.setAmount(order.getTotalAmount()); // pull the FROZEN total from the order, don't recalculate
			payment.setMethod(request.getMethod());
			payment.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
			payment.setTransactionRef(UUID.randomUUID().toString()); // stand-in for a real gateway's reference id
			payment.setPaidAt(success ? LocalDateTime.now() : null);
			payment.setCreatedAt(LocalDateTime.now());

			Payment saved = paymentRepository.save(payment);

			if (!success) {
				// In a real integration, a failed gateway call would still
				// leave a FAILED payment record (useful for support/retry
				// history) but should NOT be reported to the client as a
				// success - throwing here keeps that distinction clear.
				throw new InvalidInputException("Payment failed. Please try again.");
			}

			return toResponse(saved);
		}
		@Override
		public PaymentResponse getPaymentForOrder(Long orderId) {
			Payment payment = paymentRepository.findByOrder_OrderId(orderId)
					.orElseThrow(() -> new ResourceNotFoundException("No payment found for order: " + orderId));
			return toResponse(payment);
		}

		// Simulated gateway call - always succeeds. Swap this one method's
		// body with a real gateway SDK call later; nothing else in this class
		// would need to change.
		private boolean processGateway() {
			return true;
		}
		private PaymentResponse toResponse(Payment payment) {
			PaymentResponse dto = new PaymentResponse();
			dto.setPaymentId(payment.getPaymentId());
			dto.setOrderId(payment.getOrder().getOrderId());
			dto.setAmount(payment.getAmount());
			dto.setMethod(payment.getMethod().name());
			dto.setStatus(payment.getStatus().name());
			dto.setTransactionRef(payment.getTransactionRef());
			dto.setPaidAt(payment.getPaidAt());
			return dto;
		}
	
}

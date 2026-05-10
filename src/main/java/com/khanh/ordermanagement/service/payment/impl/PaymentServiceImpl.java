package com.khanh.ordermanagement.service.payment.impl;

import com.khanh.ordermanagement.dao.OrderDAO;
import com.khanh.ordermanagement.dao.PaymentDAO;
import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import com.khanh.ordermanagement.exception.NotFoundException;
import com.khanh.ordermanagement.model.Order;
import com.khanh.ordermanagement.model.Payment;
import com.khanh.ordermanagement.model.enums.PaymentStatus;
import com.khanh.ordermanagement.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

  private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

  private final PaymentDAO paymentDAO;
  private final OrderDAO orderDAO;

  public PaymentServiceImpl(PaymentDAO paymentDAO, OrderDAO orderDAO) {
    this.paymentDAO = paymentDAO;
    this.orderDAO = orderDAO;
  }

  @Override
  @Transactional
  public PaymentResponse create(PaymentRequest request) {
    logger.info("INFO: Processing payment for Order ID: {}", request.getOrderId());

    Order order = orderDAO.findById(request.getOrderId())
        .orElseThrow(() -> new NotFoundException("Order", request.getOrderId()));

    Payment payment = new Payment(
        order.getId(),
        order.getFinalAmount(),
        order.getPaymentMethod(),
        PaymentStatus.SUCCESS
    );

    Payment savedPayment = paymentDAO.save(payment);
    logger.info("INFO: Saved payment with ID: {}", savedPayment.getPaymentId());

    logger.info("INFO: Payment created successfully for Order: {}", order.getId());
    return mapToResponse(payment);
  }

  @Override
  @Transactional(readOnly = true)
  public PaymentResponse getDetail(String paymentId) {
    return paymentDAO.findById(paymentId)
        .map(this::mapToResponse)
        .orElseThrow(() -> new NotFoundException("Payment", paymentId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentResponse> getByOrderId(String orderId) {
    return paymentDAO.findByOrderId(orderId)
        .stream() // Chuyển Optional thành Stream để map
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateStatus(String paymentId, String status) {
    Payment payment = paymentDAO.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment", paymentId));
    try {
      paymentDAO.update(payment);
      logger.info("INFO: Updated status for Payment: {}", paymentId);
    } catch (Exception e) {
      logger.error("ERROR: Failed to update payment status", e);
    }
  }

  private PaymentResponse mapToResponse(Payment payment) {
    return new PaymentResponse(
        payment.getPaymentId(),
        payment.getOrderId(),
        payment.getFinalAmount(),
        payment.getStatus().name(),
        payment.getCreatedAt()
    );
  }
}
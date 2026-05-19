package com.khanh.ordermanagement.service.payment.impl;

import com.khanh.ordermanagement.client.PaymentGateway;
import com.khanh.ordermanagement.client.impl.BankPaymentGateway;
import com.khanh.ordermanagement.client.impl.MoMoPaymentGateway;
import com.khanh.ordermanagement.dto.request.PaymentGatewayRequest;
import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentGatewayResponse;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import com.khanh.ordermanagement.exception.NotFoundException;
import com.khanh.ordermanagement.entity.Order;
import com.khanh.ordermanagement.entity.Payment;
import com.khanh.ordermanagement.entity.enums.PaymentMethod;
import com.khanh.ordermanagement.entity.enums.PaymentStatus;
import com.khanh.ordermanagement.repository.OrderRepository;
import com.khanh.ordermanagement.repository.PaymentRepository;
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

  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;

  public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
    this.paymentRepository = paymentRepository;
    this.orderRepository = orderRepository;
  }

  @Override
  @Transactional
  public PaymentResponse create(PaymentRequest request) {
    logger.info("INFO: Processing payment for Order ID: {}", request.getOrderId());

    Order order = orderRepository.findById(request.getOrderId())
        .orElseThrow(() -> new NotFoundException("Order", request.getOrderId()));

    PaymentGateway gateway;
    if (order.getPaymentMethod() == PaymentMethod.E_WALLET) {
      gateway = new MoMoPaymentGateway();
    } else {
      gateway = new BankPaymentGateway();
    }

    PaymentGatewayRequest gatewayRequest = new PaymentGatewayRequest(order.getId(), order.getFinalAmount());
    PaymentGatewayResponse gatewayResponse = gateway.process(gatewayRequest);
    PaymentStatus status = gatewayResponse.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

    Payment payment = new Payment(
        order.getId(),
        order.getFinalAmount(),
        order.getPaymentMethod(),
        status
    );

    Payment savedPayment = paymentRepository.save(payment);

    logger.info("INFO: Payment result via {}: {} - Message: {}",
        gateway.getClass().getSimpleName(), status, gatewayResponse.getMessage());

    return mapToResponse(savedPayment);
  }

  @Override
  @Transactional(readOnly = true)
  public PaymentResponse getDetail(String paymentId) {
    return paymentRepository.findById(paymentId)
        .map(this::mapToResponse)
        .orElseThrow(() -> new NotFoundException("Payment", paymentId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PaymentResponse> getByOrderId(String orderId) {
    return paymentRepository.findByOrderId(orderId)
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateStatus(String paymentId, String status) {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment", paymentId));
    try {

      payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
      logger.info("INFO: Updated status for Payment: {} to {}", paymentId, status);

    } catch (IllegalArgumentException e) {
      logger.error("ERROR: Status truyền vào không hợp lệ: {}", status);
      throw new RuntimeException("Status không hợp lệ: " + status);
    } catch (Exception e) {
      logger.error("ERROR: Failed to update payment status", e);
      throw e;
    }
  }

  private PaymentResponse mapToResponse(Payment payment) {
    return new PaymentResponse(
        payment.getId(),
        payment.getOrderId(),
        payment.getFinalAmount(),
        payment.getStatus().name(),
        payment.getCreatedAt()
    );
  }
}
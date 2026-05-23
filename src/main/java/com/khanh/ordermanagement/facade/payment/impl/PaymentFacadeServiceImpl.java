package com.khanh.ordermanagement.facade.payment.impl;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import com.khanh.ordermanagement.entity.Order;
import com.khanh.ordermanagement.facade.payment.PaymentFacadeService;
import com.khanh.ordermanagement.service.order.OrderService;
import com.khanh.ordermanagement.service.payment.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentFacadeServiceImpl implements PaymentFacadeService {

  private final PaymentService paymentService;
  private final OrderService orderService;

  public PaymentFacadeServiceImpl(PaymentService paymentService, OrderService orderService) {
    this.paymentService = paymentService;
    this.orderService = orderService;
  }

  @Override
  @Transactional
  public PaymentResponse create(PaymentRequest request) {
    Order order = orderService.findEntityById(request.getOrderId())
        .orElseThrow(() -> new RuntimeException("Cannot find Order with ID: " + request.getOrderId()));

    return paymentService.create(request, order);
  }
}
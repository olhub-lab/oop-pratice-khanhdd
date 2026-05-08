package com.khanh.ordermanagement.service.payment.impl;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import java.util.List;
import com.khanh.ordermanagement.service.payment.PaymentService;

public class PaymentServiceImpl implements PaymentService {

  @Override
  public PaymentResponse create(PaymentRequest request) {
    return null;
  }

  @Override
  public PaymentResponse getDetail(String paymentId) {
    return null;
  }

  @Override
  public List<PaymentResponse> getByOrderId(String orderId) {
    return null;
  }

  @Override
  public void updateStatus(String paymentId, String status) {
  }
}
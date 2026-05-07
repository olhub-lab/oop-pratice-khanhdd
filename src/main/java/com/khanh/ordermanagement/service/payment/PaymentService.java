package com.khanh.ordermanagement.service.payment;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {

  PaymentResponse create(PaymentRequest request);

  PaymentResponse getDetail(String paymentId);

  List<PaymentResponse> getByOrderId(String orderId);

  void updateStatus(String paymentId, String status);
}
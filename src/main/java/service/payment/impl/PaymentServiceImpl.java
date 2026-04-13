package service.payment.impl;

import dto.request.PaymentRequest;
import dto.response.PaymentResponse;
import java.util.List;
import service.payment.PaymentService;

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
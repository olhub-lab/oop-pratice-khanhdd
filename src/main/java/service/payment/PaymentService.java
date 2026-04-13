package service.payment;

import dto.request.PaymentRequest;
import dto.response.PaymentResponse;
import java.util.List;

public interface PaymentService {

  PaymentResponse create(PaymentRequest request);

  PaymentResponse getDetail(String paymentId);

  List<PaymentResponse> getByOrderId(String orderId);

  void updateStatus(String paymentId, String status);
}
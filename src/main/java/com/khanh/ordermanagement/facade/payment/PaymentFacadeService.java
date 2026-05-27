package com.khanh.ordermanagement.facade.payment;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;


public interface PaymentFacadeService {
  PaymentResponse create(PaymentRequest request);
}
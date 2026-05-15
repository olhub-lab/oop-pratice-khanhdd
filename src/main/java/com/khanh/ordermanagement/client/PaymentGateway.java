package com.khanh.ordermanagement.client;

import com.khanh.ordermanagement.dto.request.PaymentGatewayRequest;
import com.khanh.ordermanagement.dto.response.PaymentGatewayResponse;

public interface PaymentGateway {
  PaymentGatewayResponse process(PaymentGatewayRequest request);
}
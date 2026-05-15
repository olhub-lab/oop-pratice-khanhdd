package com.khanh.ordermanagement.client.impl;

import com.khanh.ordermanagement.client.PaymentGateway;
import com.khanh.ordermanagement.dto.request.PaymentGatewayRequest;
import com.khanh.ordermanagement.dto.response.PaymentGatewayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public class MoMoPaymentGateway implements PaymentGateway {
  private static final Logger logger = LoggerFactory.getLogger(MoMoPaymentGateway.class);
  private static final BigDecimal LIMIT = new BigDecimal("10000000");

  @Override
  public PaymentGatewayResponse process(PaymentGatewayRequest request) {
    logger.info("CLIENT: MoMo is processing payment for order: {}", request.getOrderId());

    if (request.getAmount().compareTo(LIMIT) > 0) {
      return new PaymentGatewayResponse(false, "MOMO: Transaction failed - Amount exceeds 10M limit");
    }

    return new PaymentGatewayResponse(true, "MOMO: E-wallet payment successful");
  }
}
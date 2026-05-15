package com.khanh.ordermanagement.dto.request;

import java.math.BigDecimal;

public class PaymentGatewayRequest {
  private final String orderId;
  private final BigDecimal amount;

  public PaymentGatewayRequest(String orderId, BigDecimal amount) {
    this.orderId = orderId;
    this.amount = amount;
  }

  public String getOrderId() { return orderId; }
  public BigDecimal getAmount() { return amount; }
}
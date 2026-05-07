package com.khanh.ordermanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
  private final String paymentId;
  private final String orderId;
  private final BigDecimal finalAmount;
  private final String status;
  private final LocalDateTime createdAt;

  public PaymentResponse(
      String paymentId,
      String orderId,
      BigDecimal finalAmount,
      String status,
      LocalDateTime createdAt) {
    this.paymentId = paymentId;
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.status = status;
    this.createdAt = createdAt;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getStatus() {
    return status;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public String getOrderId() {
    return orderId;
  }
}
package com.khanh.ordermanagement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import com.khanh.ordermanagement.model.enums.PaymentMethod;
import com.khanh.ordermanagement.model.enums.PaymentStatus;

public class Payment {

  private final String Id;
  private final String orderId;
  private final BigDecimal finalAmount;
  private final PaymentMethod paymentMethod;
  private final LocalDateTime createdAt;
  private  PaymentStatus status;

  public Payment(
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod paymentMethod,
      PaymentStatus status) {
    this.Id = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = LocalDateTime.now();
    this.status = status;
  }

  public Payment(
      String Id,
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod paymentMethod,
      LocalDateTime createdAt,
      PaymentStatus status) {
    this.Id = Id;
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = createdAt;
    this.status = status;
  }

  public String getPaymentId() {
    return Id;
  }

  public String getOrderId() {
    return orderId;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

}
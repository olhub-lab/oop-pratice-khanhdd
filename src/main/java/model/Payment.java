package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import model.enums.PaymentMethod;
import model.enums.PaymentStatus;

public class Payment {

  private final String paymentId;
  private final String orderId;
  private final BigDecimal finalAmount;
  private final PaymentMethod paymentMethod;
  private final LocalDateTime createdAt;
  private final PaymentStatus status;

  public Payment(
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod paymentMethod,
      PaymentStatus status) {
    this.paymentId = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = LocalDateTime.now();
    this.status = status;
  }

  public Payment(
      String paymentId,
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod paymentMethod,
      LocalDateTime createdAt,
      PaymentStatus status) {
    this.paymentId = paymentId;
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = createdAt;
    this.status = status;
  }

  public String getPaymentId() {
    return paymentId;
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
}
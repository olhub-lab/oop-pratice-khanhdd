package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import model.enums.PaymentMethod;

public class Payment {

  private final String paymentId;
  private final String orderId;
  private final BigDecimal finalAmount;
  private final PaymentMethod paymentMethod;
  private final LocalDateTime createdAt;

  public Payment(String orderId, BigDecimal finalAmount, PaymentMethod paymentMethod) {

    this.paymentId = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = LocalDateTime.now();
  }

  public Payment(String paymentId, String orderId, BigDecimal finalAmount,
      PaymentMethod paymentMethod, LocalDateTime createdAt) {
    this.paymentId = paymentId;
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.createdAt = createdAt;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
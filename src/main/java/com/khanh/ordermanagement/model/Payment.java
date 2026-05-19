package com.khanh.ordermanagement.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.khanh.ordermanagement.model.enums.PaymentMethod;
import com.khanh.ordermanagement.model.enums.PaymentStatus;

@Entity
@Table(name="payments")
public class Payment {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "order_id", nullable = false)
  private String orderId;

  @Column(name = "final_amount", nullable = false)
  private  BigDecimal finalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "method")
  private  PaymentMethod method;

  @Column(name = "created_at")
  private  LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private  PaymentStatus status;

  public Payment() {
  }

  public Payment(
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod method,
      PaymentStatus status) {
    this.id = UUID.randomUUID().toString();
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.method = method;
    this.createdAt = LocalDateTime.now();
    this.status = status;
  }

  public Payment(
      String id,
      String orderId,
      BigDecimal finalAmount,
      PaymentMethod method,
      LocalDateTime createdAt,
      PaymentStatus status) {
    this.id = id;
    this.orderId = orderId;
    this.finalAmount = finalAmount;
    this.method = method;
    this.createdAt = createdAt;
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public String getOrderId() {
    return orderId;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public PaymentMethod getMethod() {
    return method;
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

  public void setMethod(PaymentMethod method) {
    this.method = method;
  }

  public void setFinalAmount(BigDecimal finalAmount) {
    this.finalAmount = finalAmount;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
package com.khanh.ordermanagement.model;

import com.khanh.ordermanagement.exception.BusinessException;
import com.khanh.ordermanagement.model.enums.OrderStatus;
import com.khanh.ordermanagement.model.enums.PaymentMethod;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @Column(name ="id", nullable = false)
  private String id;

  @Column(name ="customer_id", nullable = false)
  private String customerId;

  @Column(name ="customer_name", nullable = false)
  private String customerName;

  @Column(name ="amount", nullable = false)
  private BigDecimal amount;

  @Column(name ="fee_amount", nullable = false)
  private BigDecimal feeAmount;

  @Column(name ="discount_amount", nullable = false)
  private BigDecimal discountAmount;

  @Column(name ="final_amount", nullable = false)
  private BigDecimal finalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name ="status", nullable = false)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name ="payment_method", nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name ="created_at")
  private LocalDateTime createdAt;

  @Column(name ="updated_at")
  private LocalDateTime updatedAt;

  @Column(name ="cancel_reason")
  private String cancelReason;

  private static final BigDecimal DEFAULT_FEE = BigDecimal.ZERO;
  private static final BigDecimal DEFAULT_DISCOUNT = BigDecimal.ZERO;

  public Order() {
  }

  public Order(Customer customer, BigDecimal amount, PaymentMethod paymentMethod) {
    this.id = UUID.randomUUID().toString();
    this.customerId = customer.getId();
    this.customerName = customer.getName();
    this.amount = amount;
    this.paymentMethod = paymentMethod;

    this.status = OrderStatus.PENDING;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();

    this.feeAmount = DEFAULT_FEE;
    this.discountAmount = DEFAULT_DISCOUNT;

    this.calculateFinalAmount();
  }


  private void calculateFinalAmount() {
    this.feeAmount = this.amount.multiply(this.paymentMethod.getFeeRate());
    this.discountAmount = this.amount.multiply(this.paymentMethod.getDiscountRate());
    this.finalAmount = this.amount.add(this.feeAmount).subtract(this.discountAmount);
  }

  public void applyDiscount(BigDecimal discount) {
    if (discount == null || discount.compareTo(DEFAULT_DISCOUNT) < 0) {
      throw new IllegalArgumentException("Invalid discount");
    }
    this.discountAmount = discount;
    this.calculateFinalAmount();
    this.touch();
  }

  public void cancel(String reason) {
    if (this.status != OrderStatus.PENDING) {
      throw new BusinessException(
          "Order [" + this.id + "] cannot be cancelled. Current status: " + this.status);
    }
    if (reason == null || reason.isBlank()) {
      throw new IllegalArgumentException("Cancel reason is required");
    }
    this.status = OrderStatus.CANCELLED;
    this.cancelReason = reason;
    this.touch();
  }

  private void touch() {
    this.updatedAt = LocalDateTime.now();
  }

  public String getId() {
    return id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BigDecimal getFeeAmount() {
    return feeAmount;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
    this.calculateFinalAmount();
    this.touch();
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
    this.calculateFinalAmount();
    this.touch();
  }

  @Override
  public String toString() {
    return "Order{" + "orderId='" + id + '\'' + ", customerName='" + customerName + '\''
        + ", amount=" + amount + ", finalAmount=" + finalAmount + ", status=" + status + '}';
  }

}
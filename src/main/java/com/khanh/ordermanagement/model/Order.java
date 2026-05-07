package com.khanh.ordermanagement.model;

import com.khanh.ordermanagement.exception.BusinessException;
import com.khanh.ordermanagement.model.enums.OrderStatus;
import com.khanh.ordermanagement.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

  private final String id;
  private final String customerId;
  private final String customerName;

  private final BigDecimal amount;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;

  private OrderStatus status;
  private final PaymentMethod paymentMethod;

  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String cancelReason;

  private static final BigDecimal DEFAULT_FEE = BigDecimal.ZERO;
  private static final BigDecimal DEFAULT_DISCOUNT = BigDecimal.ZERO;

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

  @Override
  public String toString() {
    return "Order{" + "orderId='" + id + '\'' + ", customerName='" + customerName + '\''
        + ", amount=" + amount + ", finalAmount=" + finalAmount + ", status=" + status + '}';
  }

}
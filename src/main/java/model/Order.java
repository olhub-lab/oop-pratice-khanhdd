package model;

import exception.BusinessException;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

  private final String orderId;
  private final Long customerId;
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
  private static final BigDecimal FEE_RATE = new BigDecimal("0.02");

  public Order(Long customerId, String customerName, BigDecimal amount,
      PaymentMethod paymentMethod) {

    if (customerId == null) {
      throw new IllegalArgumentException("customerId is required");
    }
    if (customerName == null || customerName.isBlank()) {
      throw new IllegalArgumentException("customerName is required");
    }
    if (amount == null || amount.compareTo(DEFAULT_DISCOUNT) <= 0) {
      throw new IllegalArgumentException("amount must be > 0");
    }
    if (paymentMethod == null) {
      throw new IllegalArgumentException("paymentMethod is required");
    }

    this.orderId = UUID.randomUUID().toString();
    this.customerId = customerId;
    this.customerName = customerName;
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
    this.feeAmount = this.amount.multiply(FEE_RATE);
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
          "Order [" + this.orderId + "] cannot be cancelled. Current status: " + this.status);
    }

    this.status = OrderStatus.CANCELLED;
    this.cancelReason = (reason == null || reason.isBlank()) ? "Cancelled by user" : reason;

    this.touch();
  }

  private void touch() {
    this.updatedAt = LocalDateTime.now();
  }


  public String getOrderId() {
    return orderId;
  }

  public Long getCustomerId() {
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
    return "Order{" + "orderId='" + orderId + '\'' + ", customerName='" + customerName + '\''
        + ", amount=" + amount + ", finalAmount=" + finalAmount + ", status=" + status + '}';
  }
}
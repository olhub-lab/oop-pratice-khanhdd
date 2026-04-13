package dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

  private final String orderId;
  private final Long customerId;
  private final String customerName;
  private final BigDecimal amount;
  private final BigDecimal feeAmount;
  private final BigDecimal discountAmount;
  private final BigDecimal finalAmount;
  private final String paymentMethod;
  private final String status;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final String cancelReason;

  public OrderResponse(
      String orderId,
      Long customerId,
      String customerName,
      BigDecimal amount,
      BigDecimal feeAmount,
      BigDecimal discountAmount,
      BigDecimal finalAmount,
      String paymentMethod,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      String cancelReason) {
    this.orderId = orderId;
    this.customerId = customerId;
    this.customerName = customerName;
    this.amount = amount;
    this.feeAmount = feeAmount;
    this.discountAmount = discountAmount;
    this.finalAmount = finalAmount;
    this.paymentMethod = paymentMethod;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.cancelReason = cancelReason;
  }

  public String getOrderId() {
    return orderId;
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

  public String getStatus() {
    return status;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public String getCancelReason() {
    return cancelReason;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }
}
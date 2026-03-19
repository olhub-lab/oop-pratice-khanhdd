package entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderEntity {

  private String orderId;
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String paymentMethod;
  private String cancelReason;

  private OrderEntity(Builder builder) {
    this.orderId = builder.orderId;
    this.customerId = builder.customerId;
    this.customerName = builder.customerName;
    this.amount = builder.amount;
    this.feeAmount = builder.feeAmount;
    this.discountAmount = builder.discountAmount;
    this.finalAmount = builder.finalAmount;
    this.status = builder.status;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.paymentMethod = builder.paymentMethod;
    this.cancelReason = builder.cancelReason;
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

  public String getStatus() {
    return status;
  }

  public String getPaymentMethod() {
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

  public static class Builder {

    private String orderId;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String paymentMethod;
    private String cancelReason;

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder customerId(Long customerId) {
      this.customerId = customerId;
      return this;
    }

    public Builder customerName(String customerName) {
      this.customerName = customerName;
      return this;
    }

    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public Builder feeAmount(BigDecimal feeAmount) {
      this.feeAmount = feeAmount;
      return this;
    }

    public Builder discountAmount(BigDecimal discountAmount) {
      this.discountAmount = discountAmount;
      return this;
    }

    public Builder finalAmount(BigDecimal finalAmount) {
      this.finalAmount = finalAmount;
      return this;
    }

    public Builder status(String status) {
      this.status = status;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Builder paymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public Builder cancelReason(String cancelReason) {
      this.cancelReason = cancelReason;
      return this;
    }

    public OrderEntity build() {
      return new OrderEntity(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
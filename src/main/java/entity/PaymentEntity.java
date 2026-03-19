package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentEntity {

  private String paymentId;
  private String orderId;
  private BigDecimal finalAmount;
  private String paymentMethod;
  private LocalDateTime createdAt;
  private String status;

  public String getPaymentId() {
    return paymentId;
  }

  public String getOrderId() {
    return orderId;
  }

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getStatus() {
    return status;
  }

  private PaymentEntity(Builder builder) {
    this.paymentId = builder.paymentId;
    this.orderId = builder.orderId;
    this.finalAmount = builder.finalAmount;
    this.paymentMethod = builder.paymentMethod;
    this.createdAt = builder.createdAt;
    this.status = builder.status;
  }

  public static class Builder {

    private String paymentId;
    private String orderId;
    private BigDecimal finalAmount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String status;

    public Builder setPaymentId(String paymentId) {
      this.paymentId = paymentId;
      return this;
    }

    public Builder setOrderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder setFinalAmount(BigDecimal finalAmount) {
      this.finalAmount = finalAmount;
      return this;
    }

    public Builder setPaymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public Builder setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder setStatus(String status) {
      this.status = status;
      return this;
    }

    public PaymentEntity build() {
      return new PaymentEntity(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
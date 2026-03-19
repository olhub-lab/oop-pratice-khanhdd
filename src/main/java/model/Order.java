package model;

import exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;

public class Order {

  private String orderId;
  private Long customerId;
  private String customerName;

  private BigDecimal amount;
  private BigDecimal feeAmount;
  private BigDecimal discountAmount;
  private BigDecimal finalAmount;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private PaymentMethod paymentMethod;
  private String cancelReason;

  private static final BigDecimal DEFAULT_FEE = BigDecimal.ZERO;
  private static final BigDecimal DEFAULT_DISCOUNT = BigDecimal.ZERO;


  public Order(Long customerId, String customerName, BigDecimal amount,
      PaymentMethod paymentMethod) {

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
    this.finalAmount = amount;
  }

  private Order(Builder builder) {

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
    return this.orderId;
  }

  public Long getCustomerId() {
    return this.customerId;
  }

  public String getCustomerName() {
    return this.customerName;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public BigDecimal getFeeAmount() {
    return this.feeAmount;
  }

  public BigDecimal getDiscountAmount() {
    return this.discountAmount;
  }

  public BigDecimal getFinalAmount() {
    return this.finalAmount;
  }


  public void setFinalAmount(BigDecimal totalAmount) {
    this.finalAmount = totalAmount;
  }


  public void calculateFinalAmount() {
    this.finalAmount = this.amount.add(this.feeAmount).subtract(this.discountAmount);
  }

  public void setFeeAmount(BigDecimal feeAmount) {
    this.feeAmount = feeAmount;
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount;
  }

  public OrderStatus getStatus() {
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
    this.updatedAt = LocalDateTime.now();
  }

  public PaymentMethod getPaymentMethod() {
    return this.paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
    this.updatedAt = LocalDateTime.now();
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCancelReason() {
    return this.cancelReason;
  }

  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }

  @Override
  public String toString() {
    return "Order{" + "orderId='" + orderId + '\'' + ", customerId=" + customerId
        + ", customerName='" + customerName + '\'' + ", amount=" + amount + ", feeAmount="
        + feeAmount + ", discountAmount=" + discountAmount + ", finalAmount=" + finalAmount
        + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt="
        + updatedAt + ", paymentMethod=" + paymentMethod + '}';
  }

  public static class Builder {

    private String orderId;
    private Long customerId;
    private String customerName;

    private BigDecimal amount;
    private BigDecimal feeAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PaymentMethod paymentMethod;
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

    public Builder status(OrderStatus status) {
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

    public Builder paymentMethod(PaymentMethod paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public Builder cancelReason(String cancelReason) {
      this.cancelReason = cancelReason;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }
  public void cancel(String reason) {
    if (this.status != OrderStatus.PENDING) {
      throw new BusinessException(
          "Order [" + this.orderId + "] cannot be cancelled. Current status: " + this.status
      );
    }

    this.status = OrderStatus.CANCELLED;

    this.cancelReason = (reason == null || reason.isBlank())
        ? "Cancelled by user"
        : reason;

    this.updatedAt = LocalDateTime.now();
  }
}
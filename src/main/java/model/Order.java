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

    this.calculateFinalAmount();
  }

  private Order(Builder builder) {
    this.orderId = builder.orderId;
    this.customerId = builder.customerId;
    this.customerName = builder.customerName;
    this.amount = builder.amount;
    this.feeAmount = builder.feeAmount;
    this.discountAmount = builder.discountAmount;
    this.finalAmount = builder.finalAmount;
    this.paymentMethod = builder.paymentMethod;
    this.status = builder.status;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.cancelReason = builder.cancelReason;
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
          "Order [" + this.orderId + "] cannot be cancelled. Current status: " + this.status);
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

  public String getOrderId() { return orderId; }
  public Long getCustomerId() { return customerId; }
  public String getCustomerName() { return customerName; }
  public BigDecimal getAmount() { return amount; }
  public BigDecimal getFeeAmount() { return feeAmount; }
  public BigDecimal getDiscountAmount() { return discountAmount; }
  public BigDecimal getFinalAmount() { return finalAmount; }
  public OrderStatus getStatus() { return status; }
  public PaymentMethod getPaymentMethod() { return paymentMethod; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public LocalDateTime getUpdatedAt() { return updatedAt; }
  public String getCancelReason() { return cancelReason; }

  @Override
  public String toString() {
    return "Order{" + "orderId='" + orderId + '\'' + ", customerName='" + customerName + '\''
        + ", amount=" + amount + ", finalAmount=" + finalAmount + ", status=" + status + '}';
  }

  public static class Builder {
    private String orderId;
    private Long customerId;
    private String customerName;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cancelReason;

    public Builder orderId(String orderId) { this.orderId = orderId; return this; }
    public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
    public Builder customerName(String customerName) { this.customerName = customerName; return this; }
    public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
    public Builder feeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; return this; }
    public Builder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
    public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
    public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
    public Builder status(OrderStatus status) { this.status = status; return this; }
    public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
    public Builder cancelReason(String cancelReason) { this.cancelReason = cancelReason; return this; }

    public Order build() { return new Order(this); }
  }
}
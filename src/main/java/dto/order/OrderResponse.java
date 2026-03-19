package dto.order;

import java.math.BigDecimal;

public class OrderResponse {

  private final String orderId;
  private final String customerName;
  private final BigDecimal amount;
  private final BigDecimal finalAmount;
  private final String status;

  public OrderResponse(String orderId,
      String customerName,
      BigDecimal amount,
      BigDecimal finalAmount,
      String status) {
    this.orderId = orderId;
    this.customerName = customerName;
    this.amount = amount;
    this.finalAmount = finalAmount;
    this.status = status;
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

  public BigDecimal getFinalAmount() {
    return finalAmount;
  }

  public String getStatus() {
    return status;
  }
}
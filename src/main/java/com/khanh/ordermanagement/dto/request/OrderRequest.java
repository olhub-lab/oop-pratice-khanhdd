package com.khanh.ordermanagement.dto.request;

import com.khanh.ordermanagement.exception.BadRequestException;
import java.math.BigDecimal;
import com.khanh.ordermanagement.model.enums.PaymentMethod;

public class OrderRequest {

  private String customerId;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;

  public OrderRequest(String customerId, BigDecimal amount,
      PaymentMethod paymentMethod) {
    this.customerId = customerId;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
  }

  public String getCustomerId() {
    return customerId;
  }


  public BigDecimal getAmount() {
    return amount;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void validate() {
    if (this.customerId == null) {
      throw new BadRequestException("CustomerId must be positive");
    }

    if (this.amount == null || this.amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Amount must be > 0");
    }

    if (this.paymentMethod == null) {
      throw new BadRequestException("PaymentMethod is required");
    }
  }
}
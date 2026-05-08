package com.khanh.ordermanagement.model.enums;

import java.math.BigDecimal;

public enum PaymentMethod {
  CREDIT_CARD(new BigDecimal("0.02"), new BigDecimal("0.01")),
  BANK_TRANSFER(new BigDecimal("0.005"), BigDecimal.ZERO),
  E_WALLET(new BigDecimal("0.01"), new BigDecimal("0.03"));

  private final BigDecimal feeRate;
  private final BigDecimal discountRate;

  PaymentMethod(BigDecimal feeRate, BigDecimal discountRate) {
    this.feeRate = feeRate;
    this.discountRate = discountRate;
  }

  public BigDecimal getFeeRate() {
    return feeRate;
  }

  public BigDecimal getDiscountRate() {
    return discountRate;
  }
}
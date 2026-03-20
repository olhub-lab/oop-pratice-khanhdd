package model.enums;

import java.math.BigDecimal;

public enum PaymentMethod {
  CREDIT_CARD(new BigDecimal("0.02")),
  BANK_TRANSFER(new BigDecimal("0.00")),
  COD(new BigDecimal("0.01"));

  private final BigDecimal feeRate;

  PaymentMethod(BigDecimal feeRate) {
    this.feeRate = feeRate;
  }

  public BigDecimal getFeeRate() {
    return feeRate;
  }
}
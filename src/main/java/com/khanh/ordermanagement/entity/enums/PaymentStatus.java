package com.khanh.ordermanagement.entity.enums;

public enum PaymentStatus {
  UNKNOWN,
  SUCCESS,
  FAILED;

  public static PaymentStatus safeValueOf(String status) {
    if (status == null) {
      return UNKNOWN;
    }

    try {
      return PaymentStatus.valueOf(status);
    } catch (IllegalArgumentException e) {
      return UNKNOWN;
    }
  }
}
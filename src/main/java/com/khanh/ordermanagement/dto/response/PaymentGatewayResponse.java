package com.khanh.ordermanagement.dto.response;

public class PaymentGatewayResponse {
  private final boolean success;
  private final String message;

  public PaymentGatewayResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public boolean isSuccess() { return success; }
  public String getMessage() { return message; }
}
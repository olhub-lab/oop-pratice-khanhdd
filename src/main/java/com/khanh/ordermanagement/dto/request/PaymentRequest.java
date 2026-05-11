package com.khanh.ordermanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {
  @JsonProperty("orderId")
  private String orderId;


  public PaymentRequest() {
  }

  public PaymentRequest(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
package com.khanh.ordermanagement.dto.request;


public class PaymentRequest {

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
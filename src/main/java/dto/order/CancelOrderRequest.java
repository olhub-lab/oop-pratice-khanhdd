package dto.order;

import exception.BadRequestException;

public class CancelOrderRequest {

  private String orderId;
  private String reason;

  public CancelOrderRequest(String orderId, String reason) {
    this.orderId = orderId;
    this.reason = reason;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getReason() {
    return reason;
  }

  public void validate() {
    if (this.orderId == null || this.orderId.isBlank()) {
      throw new BadRequestException("OrderId is required");
    }
    if (this.reason == null || this.reason.isBlank()) {
      throw new BadRequestException("Cancel reason is required");
    }
    if (this.reason.length() > 500) {
      throw new BadRequestException("Cancel reason must not exceed 500 characters");
    }
  }
}
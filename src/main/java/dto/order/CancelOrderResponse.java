package dto.order;

import java.time.LocalDateTime;

public class CancelOrderResponse {

  private final String orderId;
  private final String previousStatus;
  private final String currentStatus;
  private final String reason;
  private final LocalDateTime cancelledAt;
  private final String message;

  public CancelOrderResponse(String orderId,
      String previousStatus,
      String currentStatus,
      String reason,
      LocalDateTime cancelledAt,
      String message) {
    this.orderId = orderId;
    this.previousStatus = previousStatus;
    this.currentStatus = currentStatus;
    this.reason = reason;
    this.cancelledAt = cancelledAt;
    this.message = message;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getPreviousStatus() {
    return previousStatus;
  }

  public String getCurrentStatus() {
    return currentStatus;
  }

  public String getReason() {
    return reason;
  }

  public LocalDateTime getCancelledAt() {
    return cancelledAt;
  }

  public String getMessage() {
    return message;
  }
}
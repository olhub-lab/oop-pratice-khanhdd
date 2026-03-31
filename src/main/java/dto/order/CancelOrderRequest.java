package dto.order;

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
}
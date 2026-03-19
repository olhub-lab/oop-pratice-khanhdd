package dto.order;

public class CancelOrderRequest {
  private String orderId;
  private String reason;

  public String getOrderId() { return orderId; }
  public String getReason() { return reason; }
}
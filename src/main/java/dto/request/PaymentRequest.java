package dto.request;

public class PaymentRequest {
  private String orderId;

  public PaymentRequest(String orderId) {
    this.orderId = orderId;
  }
  public String getOrderId() { return orderId; }
}
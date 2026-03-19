package dto.order;

import java.math.BigDecimal;
import model.enums.PaymentMethod;

public class OrderRequest {
  private Long customerId;
  private String customerName;
  private BigDecimal amount;
  private PaymentMethod paymentMethod;

  public Long getCustomerId() { return customerId; }
  public String getCustomerName() { return customerName; }
  public BigDecimal getAmount() { return amount; }
  public PaymentMethod getPaymentMethod() { return paymentMethod; }
}
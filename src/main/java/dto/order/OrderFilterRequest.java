package dto.order;

import java.time.LocalDateTime;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;

public class OrderFilterRequest {

  private Long customerId;
  private OrderStatus status;
  private PaymentMethod paymentMethod;

  private LocalDateTime fromDate;
  private LocalDateTime toDate;

  private Integer page = 0;
  private Integer size = 10;

  private String sort;
  public OrderFilterRequest(Long customerId, OrderStatus status, PaymentMethod paymentMethod,
      LocalDateTime fromDate, LocalDateTime toDate,
      String sort, int page, int size) {
    this.customerId = customerId;
    this.status = status;
    this.paymentMethod = paymentMethod;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.sort = sort;
    this.page = page;
    this.size = size;
  }


  public Long getCustomerId() {
    return customerId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public LocalDateTime getFromDate() {
    return fromDate;
  }

  public LocalDateTime getToDate() {
    return toDate;
  }

  public Integer getPage() {
    return page;
  }

  public Integer getSize() {
    return size;
  }

  public String getSort() {
    return sort;
  }
}
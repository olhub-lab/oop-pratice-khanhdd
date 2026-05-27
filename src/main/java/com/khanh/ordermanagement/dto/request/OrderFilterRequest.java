package com.khanh.ordermanagement.dto.request;

import java.time.LocalDateTime;
import com.khanh.ordermanagement.entity.enums.OrderStatus;
import com.khanh.ordermanagement.entity.enums.PaymentMethod;

public class OrderFilterRequest {

  private String customerId;
  private OrderStatus status;
  private PaymentMethod paymentMethod;

  private LocalDateTime fromDate;
  private LocalDateTime toDate;

  private Integer page;
  private Integer size;

  private String sort;
  public OrderFilterRequest(
      String customerId,
      OrderStatus status,
      PaymentMethod paymentMethod,
      LocalDateTime fromDate,
      LocalDateTime toDate,
      String sort,
      Integer page,
      Integer size) {
    this.customerId = customerId;
    this.status = status;
    this.paymentMethod = paymentMethod;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.sort = sort;
    this.page = page;
    this.size = size;
  }


  public String getCustomerId() {
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
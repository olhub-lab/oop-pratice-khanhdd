package service.order.impl;

import dto.request.OrderRequest;
import dto.response.CancelOrderResponse;
import dto.response.OrderResponse;
import dto.request.CancelOrderRequest;
import dto.request.OrderFilterRequest;
import dto.response.PageResponse;
import service.order.OrderService;


public class OrderServiceImpl implements OrderService {

  @Override
  public OrderResponse create(OrderRequest request) {
    return null;
  }

  @Override
  public OrderResponse getDetail(String orderId) {
    return null;
  }

  @Override
  public PageResponse<OrderResponse> list(OrderFilterRequest request) {
    return null;
  }

  @Override
  public CancelOrderResponse cancel(CancelOrderRequest request) {
    return null;
  }

}
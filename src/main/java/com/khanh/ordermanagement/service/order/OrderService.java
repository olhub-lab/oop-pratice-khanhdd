  package com.khanh.ordermanagement.service.order;

  import com.khanh.ordermanagement.dto.request.CancelOrderRequest;
  import com.khanh.ordermanagement.dto.response.CancelOrderResponse;
  import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
  import com.khanh.ordermanagement.dto.request.OrderRequest;
  import com.khanh.ordermanagement.dto.response.OrderResponse;
  import com.khanh.ordermanagement.dto.response.PageResponse;

  public interface OrderService {

    OrderResponse create(OrderRequest request);

    OrderResponse getDetail(String orderId);

    PageResponse<OrderResponse> list(OrderFilterRequest filterRequest);

    CancelOrderResponse cancel(CancelOrderRequest request);
  }
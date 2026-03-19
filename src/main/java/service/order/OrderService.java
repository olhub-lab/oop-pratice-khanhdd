package service.order;

import dto.order.CancelOrderRequest;
import dto.order.CancelOrderResponse;
import dto.order.OrderFilterRequest;
import dto.order.OrderRequest;
import dto.order.OrderResponse;
import dto.order.PageResponse;

public interface OrderService {

  OrderResponse createOrder(OrderRequest request);

  OrderResponse getOrderDetail(String orderId);

  PageResponse<OrderResponse> listOrders(OrderFilterRequest filterRequest);

  CancelOrderResponse cancelOrder(CancelOrderRequest request);
}
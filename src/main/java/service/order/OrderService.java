  package service.order;

  import dto.request.CancelOrderRequest;
  import dto.response.CancelOrderResponse;
  import dto.request.OrderFilterRequest;
  import dto.request.OrderRequest;
  import dto.response.OrderResponse;
  import dto.response.PageResponse;

  public interface OrderService {

    OrderResponse create(OrderRequest request);

    OrderResponse getDetail(String orderId);

    PageResponse<OrderResponse> list(OrderFilterRequest filterRequest);

    CancelOrderResponse cancel(CancelOrderRequest request);
  }
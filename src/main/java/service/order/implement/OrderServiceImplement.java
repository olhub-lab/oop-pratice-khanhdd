package service.order.implement;

import dto.order.*;
import exception.BadRequestException;
import exception.NotFoundException;
import model.Order;
import model.enums.OrderStatus;
import repository.OrderRepository;
import service.order.OrderService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImplement implements OrderService {

  private final OrderRepository repository;

  public OrderServiceImplement(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public OrderResponse createOrder(OrderRequest request) {
    this.validateCreateRequest(request);

    Order order = Order.builder()
        .customerId(request.getCustomerId())
        .customerName(request.getCustomerName())
        .amount(request.getAmount())
        .paymentMethod(request.getPaymentMethod())
        .build();

    repository.save(order);

    return mapToResponse(order);
  }

  @Override
  public OrderResponse getOrderDetail(String orderId) {
    this.validateOrderId(orderId);

    Order order = repository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

    return mapToResponse(order);
  }

  @Override
  public PageResponse<OrderResponse> listOrders(OrderFilterRequest request) {

    List<Order> orders = repository.findAll();


    List<Order> filtered = orders.stream()
        .filter(o -> request.getCustomerId() == null || o.getCustomerId().equals(request.getCustomerId()))
        .filter(o -> request.getStatus() == null || o.getStatus() == request.getStatus())
        .filter(o -> request.getPaymentMethod() == null || o.getPaymentMethod() == request.getPaymentMethod())
        .filter(o -> request.getFromDate() == null || !o.getCreatedAt().isBefore(request.getFromDate()))
        .filter(o -> request.getToDate() == null || !o.getCreatedAt().isAfter(request.getToDate()))
        .collect(Collectors.toList());


    Comparator<Order> comparator = Comparator.comparing(Order::getCreatedAt).reversed();

    if ("amount_asc".equals(request.getSort())) {
      comparator = Comparator.comparing(Order::getAmount);
    }

    filtered.sort(comparator);


    int page = request.getPage();
    int size = request.getSize();

    int totalElements = filtered.size();
    int fromIndex = Math.min(page * size, totalElements);
    int toIndex = Math.min(fromIndex + size, totalElements);

    List<OrderResponse> content = filtered.subList(fromIndex, toIndex)
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());

    int totalPages = (int) Math.ceil((double) totalElements / size);

    return new PageResponse<>(
        content,
        totalElements,
        totalPages,
        page < totalPages - 1,
        page > 0
    );
  }


  @Override
  public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
    this.validateCancelRequest(request);

    Order order = repository.findById(request.getOrderId())
        .orElseThrow(() -> new NotFoundException("Order not found"));

    OrderStatus oldStatus = order.getStatus();

    order.cancel(request.getReason());

    repository.update(order);

    return new CancelOrderResponse(
        order.getOrderId(),
        oldStatus.name(),
        order.getStatus().name(),
        order.getCancelReason(),
        order.getUpdatedAt(),
        "Order cancelled successfully"
    );
  }

  private void validateCreateRequest(OrderRequest request) {
    if (request == null || request.getAmount() == null) {
      throw new BadRequestException("Invalid request");
    }

    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Amount must be > 0");
    }
  }

  private void validateOrderId(String orderId) {
    if (orderId == null || orderId.isBlank()) {
      throw new BadRequestException("OrderId is required");
    }
  }

  private void validateCancelRequest(CancelOrderRequest request) {
    if (request == null || request.getOrderId() == null) {
      throw new BadRequestException("Invalid cancel request");
    }
  }


  private OrderResponse mapToResponse(Order order) {
    return new OrderResponse(
        order.getOrderId(),
        order.getCustomerName(),
        order.getAmount(),
        order.getFinalAmount(),
        order.getStatus().name()
    );
  }
}
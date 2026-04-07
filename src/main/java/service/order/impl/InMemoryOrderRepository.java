package service.order.impl;

import dto.order.OrderRequest;
import dto.order.CancelOrderResponse;
import dto.order.OrderResponse;
import dto.order.CancelOrderRequest;
import dto.order.OrderFilterRequest;
import dto.order.PageResponse;
import exception.BadRequestException;
import exception.NotFoundException;
import model.Order;
import model.enums.OrderStatus;
import repository.OrderRepository;
import service.order.OrderService;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderService {

  private static final Logger logger = Logger.getLogger(InMemoryOrderRepository.class.getName());
  private static final String SORT_AMOUNT_ASC = "amount_asc";
  private static final String SORT_AMOUNT_DESC = "amount_desc";
  private final OrderRepository repository;

  public InMemoryOrderRepository(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public OrderResponse createOrder(OrderRequest request) {
    logger.info(() -> "Create order request: " + request);

    if (request == null) {
      throw new BadRequestException("Request must not be null");
    }

    request.validate();

    Order order = new Order(request.getCustomerId(), request.getCustomerName(), request.getAmount(),
        request.getPaymentMethod());

    repository.save(order);

    logger.info(() -> "Order created successfully: orderId=" + order.getOrderId());

    return this.mapToResponse(order);
  }

  @Override
  public OrderResponse getOrderDetail(String orderId) {
    logger.info(() -> "Get order detail: orderId=" + orderId);

    this.validateOrderId(orderId);

    Order order = repository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

    return this.mapToResponse(order);
  }

  @Override
  public PageResponse<OrderResponse> listOrders(OrderFilterRequest request) {
    logger.info(() -> "List orders with filter");

    if (request.getFromDate() != null && request.getToDate() != null) {
      if (request.getFromDate().isAfter(request.getToDate())) {
        throw new BadRequestException("fromDate must be before or equal to toDate");
      }
    }

    List<Order> orders = repository.findAll();

    List<Order> filtered = orders.stream().filter(
            o -> request.getCustomerId() == null || o.getCustomerId().equals(request.getCustomerId()))
        .filter(o -> request.getStatus() == null || o.getStatus() == request.getStatus()).filter(
            o -> request.getPaymentMethod() == null
                || o.getPaymentMethod() == request.getPaymentMethod()).filter(
            o -> request.getFromDate() == null || !o.getCreatedAt().isBefore(request.getFromDate()))
        .filter(o -> request.getToDate() == null || !o.getCreatedAt().isAfter(request.getToDate()))
        .collect(Collectors.toList());

    Comparator<Order> comparator = Comparator.comparing(Order::getCreatedAt).reversed();

    if (SORT_AMOUNT_ASC.equals(request.getSort())) {
      comparator = Comparator.comparing(Order::getAmount);
    } else if (SORT_AMOUNT_DESC.equals(request.getSort())) {
      comparator = Comparator.comparing(Order::getAmount).reversed();
    }

    filtered.sort(comparator);

    int page = request.getPage() < 0 ? 0 : request.getPage();
    int size = request.getSize() <= 0 ? 10 : Math.min(request.getSize(), 100);

    int totalElements = filtered.size();
    int fromIndex = Math.min(page * size, totalElements);
    int toIndex = Math.min(fromIndex + size, totalElements);

    List<OrderResponse> content = filtered.subList(fromIndex, toIndex).stream()
        .map(this::mapToResponse).collect(Collectors.toList());

    int totalPages = (int) Math.ceil((double) totalElements / size);

    logger.info(() -> "List orders success: totalElements=" + totalElements);

    return new PageResponse<>(content, totalElements, totalPages, page < totalPages - 1, page > 0);
  }

  @Override
  public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
    logger.info(() -> "Cancel order: orderId=" + request.getOrderId());

    this.validateCancelRequest(request);

    Order order = repository.findById(request.getOrderId()).orElseThrow(
        () -> new NotFoundException("Order not found with id: " + request.getOrderId()));

    OrderStatus oldStatus = order.getStatus();

    order.cancel(request.getReason());
    repository.update(order);

    logger.info(() -> "Order cancelled: orderId=" + order.getOrderId());

    return new CancelOrderResponse(order.getOrderId(), oldStatus.name(), order.getStatus().name(),
        order.getCancelReason(), order.getUpdatedAt(), "Order cancelled successfully");
  }

  private void validateOrderId(String orderId) {
    if (orderId == null || orderId.isBlank()) {
      throw new BadRequestException("OrderId is required");
    }
  }

  private void validateCancelRequest(CancelOrderRequest request) {
    if (request == null || request.getOrderId() == null || request.getOrderId().isBlank()) {
      throw new BadRequestException("OrderId is required");
    }
    if (request.getReason() == null || request.getReason().isBlank()) {
      throw new BadRequestException("Cancel reason is required");
    }
    if (request.getReason().length() > 500) {
      throw new BadRequestException("Cancel reason must not exceed 500 characters");
    }
  }


  private OrderResponse mapToResponse(Order order) {
    return new OrderResponse(order.getOrderId(), order.getCustomerId(), order.getCustomerName(),
        order.getAmount(), order.getFeeAmount(), order.getDiscountAmount(), order.getFinalAmount(),
        order.getPaymentMethod().name(), order.getStatus().name(), order.getCreatedAt(),
        order.getUpdatedAt(), order.getCancelReason());
  }
}
  package com.khanh.ordermanagement.service.order.impl;

  import com.khanh.ordermanagement.dao.OrderDAO;
  import com.khanh.ordermanagement.dao.CustomerDAO;
  import com.khanh.ordermanagement.dto.request.CancelOrderRequest;
  import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
  import com.khanh.ordermanagement.dto.request.OrderRequest;
  import com.khanh.ordermanagement.dto.response.CancelOrderResponse;
  import com.khanh.ordermanagement.dto.response.OrderResponse;
  import com.khanh.ordermanagement.dto.response.PageResponse;
  import com.khanh.ordermanagement.exception.BadRequestException;
  import com.khanh.ordermanagement.exception.NotFoundException;
  import com.khanh.ordermanagement.exception.database.ServiceException;
  import com.khanh.ordermanagement.model.Order;
  import com.khanh.ordermanagement.model.Customer;
  import com.khanh.ordermanagement.service.order.OrderService;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;

  import java.util.List;
  import java.util.stream.Collectors;

  @Service
  public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDAO orderDAO;
    private final CustomerDAO customerDAO;

    public OrderServiceImpl(OrderDAO orderDAO, CustomerDAO customerDAO) {
      this.orderDAO = orderDAO;
      this.customerDAO = customerDAO;
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
      logger.info("INFO: Creating new order for customer: {}", request.getCustomerId());
      request.validate();

      Customer customer = customerDAO.findById(String.valueOf(request.getCustomerId())).orElseThrow(
          () -> new NotFoundException("Customer", String.valueOf(request.getCustomerId())));

      try {
        Order order = new Order(customer, request.getAmount(), request.getPaymentMethod());
        Order saved = orderDAO.save(order);

        logger.info("INFO: Order {} created successfully.", saved.getId());
        return mapToResponse(saved);
      } catch (Exception e) {
        logger.error("ERROR: Failed to create order", e);
        throw new ServiceException("Internal system error during order creation", e);
      }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> list(OrderFilterRequest request) {
      logger.info("INFO: Listing orders with DB-level pagination and filters.");

      if (request.getFromDate() != null && request.getToDate() != null && request.getFromDate().isAfter(request.getToDate())) {
        throw new BadRequestException("fromDate must be before toDate");
      }


      List<Order> orders = orderDAO.findAll(request);
      int totalElements = orderDAO.count(request);

      List<OrderResponse> content = orders.stream()
          .map(this::mapToResponse)
          .collect(Collectors.toList());

      int size = request.getSize();
      int page = request.getPage();
      int totalPages = (totalElements == 0) ? 0 : (int) Math.ceil((double) totalElements / size);

      return new PageResponse<>(
          content,
          totalElements,
          totalPages,
          page,
          size,
          page < totalPages - 1,
          page > 0
      );
    }

    @Override
    @Transactional
    public CancelOrderResponse cancel(CancelOrderRequest request) {
      logger.info("INFO: Cancelling order: {}", request.getOrderId());
      request.validate();

      Order order = orderDAO.findById(request.getOrderId())
          .orElseThrow(() -> new NotFoundException("Order", request.getOrderId()));

      String oldStatus = order.getStatus().name();

      order.cancel(request.getReason());
      orderDAO.update(order);

      logger.info("INFO: Order {} status changed: {} -> CANCELLED", order.getId(), oldStatus);
      return new CancelOrderResponse(
          order.getId(), oldStatus,
          order.getStatus().name(),
          order.getCancelReason(),
          order.getUpdatedAt(),
          "Order cancelled successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getDetail(String orderId) {
      return orderDAO.findById(orderId).map(this::mapToResponse)
          .orElseThrow(() -> new NotFoundException("Order", orderId));
    }

    private OrderResponse mapToResponse(Order order) {
      return new OrderResponse(
          order.getId(),
          order.getCustomerId(),
          order.getCustomerName(),
          order.getAmount(),
          order.getFeeAmount(),
          order.getDiscountAmount(),
          order.getFinalAmount(),
          order.getPaymentMethod().name(),
          order.getStatus().name(),
          order.getCreatedAt(),
          order.getUpdatedAt(),
          order.getCancelReason());
    }
  }
  package com.khanh.ordermanagement.service.order.impl;

  import com.khanh.ordermanagement.dto.request.CancelOrderRequest;
  import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
  import com.khanh.ordermanagement.dto.request.OrderRequest;
  import com.khanh.ordermanagement.dto.response.CancelOrderResponse;
  import com.khanh.ordermanagement.dto.response.OrderResponse;
  import com.khanh.ordermanagement.dto.response.PageResponse;
  import com.khanh.ordermanagement.exception.BadRequestException;
  import com.khanh.ordermanagement.exception.NotFoundException;
  import com.khanh.ordermanagement.exception.database.ServiceException;
  import com.khanh.ordermanagement.entity.Order;
  import com.khanh.ordermanagement.entity.Customer;
  import com.khanh.ordermanagement.repository.CustomerRepository;
  import com.khanh.ordermanagement.repository.OrderRepository;
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

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository) {
      this.orderRepository = orderRepository;
      this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
      logger.info("INFO: Creating new order for customer: {}", request.getCustomerId());
      request.validate();

      Customer customer = customerRepository.findById(String.valueOf(request.getCustomerId()))
          .orElseThrow(() -> new NotFoundException("Customer", String.valueOf(request.getCustomerId())));

      try {
        Order order = new Order(customer, request.getAmount(), request.getPaymentMethod());

        Order saved = orderRepository.save(order);

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
      logger.info("INFO: Listing orders with JPA-level pagination and dynamic filters.");

      if (request.getFromDate() != null && request.getToDate() != null && request.getFromDate().isAfter(request.getToDate())) {
        throw new BadRequestException("fromDate must be before toDate");
      }

      org.springframework.data.domain.Pageable pageable =
          org.springframework.data.domain.PageRequest.of(request.getPage(), request.getSize());

      org.springframework.data.domain.Page<Order> orderPage = orderRepository.findWithFilter(
          request.getStatus(),
          request.getCustomerId(),
          request.getFromDate(),
          request.getToDate(),
          pageable
      );

      List<OrderResponse> content = orderPage.getContent().stream()
          .map(this::mapToResponse)
          .collect(Collectors.toList());

      return new PageResponse<>(
          content,
          (int) orderPage.getTotalElements(),
          orderPage.getTotalPages(),
          request.getPage(),
          request.getSize(),
          orderPage.hasNext(),
          orderPage.hasPrevious()
      );
    }

    @Override
    @Transactional
    public CancelOrderResponse cancel(CancelOrderRequest request) {
      logger.info("INFO: Cancelling order: {}", request.getOrderId());
      request.validate();

      Order order = orderRepository.findById(request.getOrderId())
          .orElseThrow(() -> new NotFoundException("Order", request.getOrderId()));

      String oldStatus = order.getStatus().name();

      order.cancel(request.getReason());

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
      return orderRepository.findById(orderId).map(this::mapToResponse)
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
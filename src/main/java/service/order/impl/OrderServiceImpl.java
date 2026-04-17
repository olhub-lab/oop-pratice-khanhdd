package service.order.impl;

import config.DBConnection;
import config.DatabaseUtil;
import dao.OrderDAO;
import dao.CustomerDAO;
import dto.request.*;
import dto.response.*;
import exception.*;
import exception.database.ServiceException;
import model.Order;
import model.Customer;
import model.enums.OrderStatus;
import service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
  private static final String SORT_AMOUNT_ASC = "amount_asc";
  private static final String SORT_AMOUNT_DESC = "amount_desc";

  private final OrderDAO orderDAO;
  private final CustomerDAO customerDAO;

  public OrderServiceImpl(OrderDAO orderDAO, CustomerDAO customerDAO) {
    this.orderDAO = orderDAO;
    this.customerDAO = customerDAO;
  }

  @Override
  public OrderResponse create(OrderRequest request) {
    logger.info("INFO: Entering create(OrderRequest) method.");
    request.validate();

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      logger.debug("DEBUG: Checking customer: {}", request.getCustomerId());

      Customer customer = customerDAO.findById(String.valueOf(request.getCustomerId()))
          .orElseThrow(() -> {
            logger.warn("WARNING: Customer not found: {}", request.getCustomerId());
            return new NotFoundException("Customer", String.valueOf(request.getCustomerId()));
          });

      DatabaseUtil.beginTransaction(conn);
      Order order = new Order(customer, request.getAmount(), request.getPaymentMethod());
      Order saved = orderDAO.save(conn, order);
      DatabaseUtil.commitTransaction(conn);

      logger.info("INFO: Order created successfully with ID: {}", saved.getId());
      return mapToResponse(saved);
    } catch (Exception e) {
      DatabaseUtil.rollbackTransaction(conn);
      logger.error("ERROR: Create process failed", e);
      throw (e instanceof RuntimeException) ? (RuntimeException) e : new ServiceException("System error", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public PageResponse<OrderResponse> list(OrderFilterRequest request) {
    logger.info("INFO: Entering list(OrderFilterRequest) method.");

    if (request.getFromDate() != null && request.getToDate() != null && request.getFromDate().isAfter(request.getToDate())) {
      throw new BadRequestException("fromDate must be before toDate");
    }

    List<Order> orders = orderDAO.findAll(request);

    List<Order> filtered = orders.stream()
        .filter(o -> request.getCustomerId() == null || o.getCustomerId().equals(request.getCustomerId()))
        .filter(o -> request.getStatus() == null || o.getStatus() == request.getStatus())
        .filter(o -> request.getPaymentMethod() == null || o.getPaymentMethod() == request.getPaymentMethod())
        .filter(o -> request.getFromDate() == null || !o.getCreatedAt().isBefore(request.getFromDate()))
        .filter(o -> request.getToDate() == null || !o.getCreatedAt().isAfter(request.getToDate()))
        .collect(Collectors.toList());


    Comparator<Order> comparator = Comparator.comparing(Order::getCreatedAt).reversed();
    if (SORT_AMOUNT_ASC.equals(
        request.getSort())) comparator =
        Comparator.comparing(Order::getAmount);
    else if (SORT_AMOUNT_DESC.equals(
        request.getSort())) comparator =
        Comparator.comparing(Order::getAmount).reversed();
    filtered.sort(comparator);

    int page = Math.max(request.getPage(), 0);
    int size = (request.getSize() <= 0) ? 10 : Math.min(request.getSize(), 100);
    int totalElements = filtered.size();
    int fromIndex = Math.min(page * size, totalElements);
    int toIndex = Math.min(fromIndex + size, totalElements);

    List<OrderResponse> content = filtered.subList(fromIndex, toIndex).stream()
        .map(this::mapToResponse).collect(Collectors.toList());

    int totalPages = (int) Math.ceil((double) totalElements / size);
    logger.info("INFO: List success. Total elements: {}", totalElements);

    return new PageResponse<>(content, totalElements, totalPages, page < totalPages - 1, page > 0);
  }

  @Override
  public CancelOrderResponse cancel(CancelOrderRequest request) {
    logger.info("INFO: Entering cancel(CancelOrderRequest) method.");
    request.validate();

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      Order order = orderDAO.findById(request.getOrderId())
          .orElseThrow(() -> new NotFoundException("Order", request.getOrderId()));

      String oldStatus = order.getStatus().name();
      DatabaseUtil.beginTransaction(conn);

      order.cancel(request.getReason());
      orderDAO.update(conn, order);

      DatabaseUtil.commitTransaction(conn);
      logger.info("INFO: Order {} cancelled.", order.getId());

      return new CancelOrderResponse(
          order.getId(),
          oldStatus,
          order.getStatus().name(),
          order.getCancelReason(),
          order.getUpdatedAt(), "Order cancelled successfully");
    } catch (BusinessException | IllegalArgumentException e) {
      DatabaseUtil.rollbackTransaction(conn);
      logger.warn("WARNING: Business rule violation: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      DatabaseUtil.rollbackTransaction(conn);
      logger.error("ERROR: Cancel failed", e);
      throw new ServiceException("System error", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public OrderResponse getDetail(String orderId) {
    logger.info("INFO: Entering getDetail(String) method.");
    return orderDAO.findById(orderId)
        .map(this::mapToResponse)
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
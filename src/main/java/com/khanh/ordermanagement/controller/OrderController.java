package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.CancelOrderRequest;
import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
import com.khanh.ordermanagement.dto.request.OrderRequest;
import com.khanh.ordermanagement.dto.response.CancelOrderResponse;
import com.khanh.ordermanagement.dto.response.OrderResponse;
import com.khanh.ordermanagement.dto.response.PageResponse;
import com.khanh.ordermanagement.facade.order.OrderFacadeService;
import com.khanh.ordermanagement.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
  private final OrderService orderService;
  private final OrderFacadeService orderFacadeService;

  public OrderController(OrderService orderService, OrderFacadeService orderFacadeService) {
    this.orderService = orderService;
    this.orderFacadeService = orderFacadeService;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
    logger.info("REST: Request to create order for customer: {}", request.getCustomerId());
    logger.debug("REST: Order Detail: {}", request);

    return new ResponseEntity<>(orderFacadeService.create(request), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
    logger.info("REST: Getting order detail for ID: {}", id);

    return ResponseEntity.ok(orderService.getDetail(id));
  }

  @GetMapping
  public ResponseEntity<PageResponse<OrderResponse>> list(OrderFilterRequest request) {
    logger.info("REST: Request list orders with filters");
    logger.debug("REST: Filter params: {}", request);

    return ResponseEntity.ok(orderService.list(request));
  }

  @PutMapping("/cancel")
  public ResponseEntity<CancelOrderResponse> cancel(@RequestBody CancelOrderRequest request) {
    logger.info("REST: Request to cancel order ID: {}", request.getOrderId());
    logger.debug("REST: Cancel reason: {}", request.getReason());

    return ResponseEntity.ok(orderService.cancel(request));
  }
}
package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.CancelOrderRequest;
import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
import com.khanh.ordermanagement.dto.request.OrderRequest;
import com.khanh.ordermanagement.dto.response.CancelOrderResponse;
import com.khanh.ordermanagement.dto.response.OrderResponse;
import com.khanh.ordermanagement.dto.response.PageResponse;
import com.khanh.ordermanagement.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
    return new ResponseEntity<>(orderService.create(request), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getDetail(@PathVariable String id) {
    return ResponseEntity.ok(orderService.getDetail(id));
  }

  @GetMapping
  public ResponseEntity<PageResponse<OrderResponse>> list(OrderFilterRequest request) {
    return ResponseEntity.ok(orderService.list(request));
  }

  @PutMapping("/cancel")
  public ResponseEntity<CancelOrderResponse> cancel(@RequestBody CancelOrderRequest request) {
    return ResponseEntity.ok(orderService.cancel(request));
  }
}
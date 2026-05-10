package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import com.khanh.ordermanagement.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping
  public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) {
    logger.info("REST: Request to create payment for Order ID: {}", request.getOrderId());
    PaymentResponse response = paymentService.create(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponse> getDetail(@PathVariable String paymentId) {
    logger.info("REST: Request to get payment detail: {}", paymentId);
    return ResponseEntity.ok(paymentService.getDetail(paymentId));
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<PaymentResponse>> getByOrderId(@PathVariable String orderId) {
    logger.info("REST: Request to get payments for Order ID: {}", orderId);
    List<PaymentResponse> responses = paymentService.getByOrderId(orderId);
    return ResponseEntity.ok(responses);
  }

  @PutMapping("/{paymentId}/status")
  public ResponseEntity<Void> updateStatus(
      @PathVariable String paymentId,
      @RequestParam String status) {
    logger.info("REST: Request to update payment {} status to {}", paymentId, status);
    paymentService.updateStatus(paymentId, status);
    return ResponseEntity.noContent().build();
  }
}
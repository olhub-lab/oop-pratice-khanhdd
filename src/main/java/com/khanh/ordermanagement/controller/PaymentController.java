package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.PaymentRequest;
import com.khanh.ordermanagement.dto.response.PaymentResponse;
import com.khanh.ordermanagement.facade.payment.PaymentFacadeService;
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
  private final PaymentFacadeService paymentFacadeService;

  public PaymentController(PaymentService paymentService, PaymentFacadeService paymentFacadeService) {

    this.paymentService = paymentService;
    this.paymentFacadeService = paymentFacadeService;
  }

  @PostMapping
  public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) {
    logger.info("REST: Request to process payment for Order: {}", request.getOrderId());
    logger.debug("REST: Payment request data: {}", request);
    return new ResponseEntity<>(paymentFacadeService.create(request), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentResponse> getById(@PathVariable String id) {
    logger.info("REST: Getting payment detail for ID: {}", id);
    return ResponseEntity.ok(paymentService.getDetail(id));
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<PaymentResponse>> getByOrderId(@PathVariable String orderId) {
    logger.info("REST: Fetching payment history for Order: {}", orderId);
    return ResponseEntity.ok(paymentService.getByOrderId(orderId));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<Void> updateStatus(
      @PathVariable String id,
      @RequestParam String status) {
    logger.info("REST: Manually updating payment {} to status {}", id, status);
    paymentService.updateStatus(id, status);
    return ResponseEntity.noContent().build();
  }
}
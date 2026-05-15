package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import com.khanh.ordermanagement.dto.response.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.khanh.ordermanagement.service.customer.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
    logger.info("REST: Request to create new customer");
    logger.debug("REST: Customer Data: {}", request);
    CustomerResponse response = customerService.create(request);
    logger.info("REST: Customer created successfully with ID: {}", response.getId());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable String id) {
    logger.info("REST: Fetching customer by ID: {}", id);
    return ResponseEntity.ok(customerService.getById(id));
  }

  @GetMapping
  public ResponseEntity<PageResponse<CustomerResponse>> getAll(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size
  ) {
    logger.info("REST: Request to get all customers - Page: {}, Size: {}", page, size);
    return ResponseEntity.ok(customerService.getAll(page, size));
  }
}
package com.khanh.ordermanagement.controller;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.khanh.ordermanagement.service.customer.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<CustomerResponse> create(@RequestBody CustomerRequest request) {
    return new ResponseEntity<>(customerService.create(request), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable String id) {
    return ResponseEntity.ok(customerService.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> getAll() {
    return ResponseEntity.ok(customerService.getAll());
  }
}
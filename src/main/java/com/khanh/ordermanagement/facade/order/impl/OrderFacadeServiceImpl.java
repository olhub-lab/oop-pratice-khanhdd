package com.khanh.ordermanagement.facade.order.impl;

import com.khanh.ordermanagement.dto.request.OrderRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import com.khanh.ordermanagement.dto.response.OrderResponse;
import com.khanh.ordermanagement.entity.Customer;
import com.khanh.ordermanagement.exception.NotFoundException;
import com.khanh.ordermanagement.facade.order.OrderFacadeService;
import com.khanh.ordermanagement.service.customer.CustomerService;
import com.khanh.ordermanagement.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderFacadeServiceImpl implements OrderFacadeService {

  private final OrderService orderService;
  private final CustomerService customerService;

  public OrderFacadeServiceImpl(OrderService orderService, CustomerService customerService) {
    this.orderService = orderService;
    this.customerService = customerService;
  }

  @Override
  @Transactional
  public OrderResponse create(OrderRequest request) {
    Customer customer = customerService.findById(request.getCustomerId())
        .orElseThrow(() -> new NotFoundException("Customer", request.getCustomerId()));

    request.setCustomerName(customer.getName());

    return orderService.create(request);
  }
}
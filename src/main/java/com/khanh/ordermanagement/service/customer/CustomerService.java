package com.khanh.ordermanagement.service.customer;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import java.util.List;

public interface CustomerService {

  CustomerResponse create(CustomerRequest request);

  CustomerResponse getById(String id);

  CustomerResponse getByPhone(String phone);

  List<CustomerResponse> getAll();
}
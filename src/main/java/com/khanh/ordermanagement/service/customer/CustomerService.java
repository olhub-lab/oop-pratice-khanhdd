package com.khanh.ordermanagement.service.customer;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import com.khanh.ordermanagement.dto.response.PageResponse;
import java.util.List;

public interface CustomerService {

  CustomerResponse create(CustomerRequest request);

  CustomerResponse getById(String id);

  CustomerResponse getByPhone(String phone);

  PageResponse<CustomerResponse> getAll(int page, int size);
}
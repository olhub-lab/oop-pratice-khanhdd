package service.customer.impl;

import dto.request.CustomerRequest;
import dto.response.CustomerResponse;
import java.util.List;
import service.customer.CustomerService;

public class CustomerServiceImpl implements CustomerService {

  @Override
  public CustomerResponse create(CustomerRequest request) {
    return null;
  }

  @Override
  public CustomerResponse getById(Long id) {
    return null;
  }

  @Override
  public CustomerResponse getByPhone(String phone) {
    return null;
  }

  @Override
  public List<CustomerResponse> getAll() {
    return null;
  }
}
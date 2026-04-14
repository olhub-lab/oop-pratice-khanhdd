package service.customer.impl;

import dao.CustomerDAO;
import dto.request.CustomerRequest;
import dto.response.CustomerResponse;
import exception.NotFoundException;
import model.Customer;
import service.customer.CustomerService;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

  private final CustomerDAO customerDAO;

  public CustomerServiceImpl(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }

  @Override
  public CustomerResponse create(CustomerRequest request) {
    request.validate();
    Customer customer = new Customer(
        java.util.UUID.randomUUID().toString(),
        request.getName(),
        request.getPhone()
    );

    Customer savedCustomer = customerDAO.save(customer);
    return mapToResponse(savedCustomer);
  }

  @Override
  public CustomerResponse getById(String id) {
    return customerDAO.findById(id)
        .map(this::mapToResponse)
        .orElseThrow(() -> new NotFoundException("Customer", id));
  }

  @Override
  public List<CustomerResponse> getAll() {
    return customerDAO.findAll().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerResponse getByPhone(String phone) {
    return customerDAO.findByPhone(phone)
        .map(this::mapToResponse)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng với số điện thoại: " + phone));
  }


  private CustomerResponse mapToResponse(Customer customer) {
    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getPhone(),
        customer.getCreatedAt()
    );
  }
}
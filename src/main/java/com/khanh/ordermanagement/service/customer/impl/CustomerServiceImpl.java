package com.khanh.ordermanagement.service.customer.impl;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import com.khanh.ordermanagement.dto.response.PageResponse;
import com.khanh.ordermanagement.exception.NotFoundException;
import com.khanh.ordermanagement.exception.database.DuplicateRecordException;
import com.khanh.ordermanagement.exception.database.ServiceException;
import com.khanh.ordermanagement.entity.Customer;
import com.khanh.ordermanagement.repository.CustomerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.khanh.ordermanagement.service.customer.CustomerService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

  private final CustomerRepository customerRepository;

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  @Transactional
  public CustomerResponse create(CustomerRequest request) {
    logger.info("INFO: Entering create(CustomerRequest) method for: {}", request.getName());

    request.validate();

    logger.debug("DEBUG: Checking if phone {} already exists", request.getPhone());
    customerRepository.findByPhone(request.getPhone()).ifPresent(c -> {
      logger.warn("WARNING: Phone number {} is already registered", request.getPhone());
      throw new DuplicateRecordException("Phone number already registered.");
    });

    try {
      Customer customer = new Customer(
          UUID.randomUUID().toString(),
          request.getName(),
          request.getPhone()
      );

      Customer savedCustomer = customerRepository.save(customer);

      logger.info("INFO: Customer created successfully with ID: {}", savedCustomer.getId());
      return mapToResponse(savedCustomer);

    } catch (Exception e) {
      logger.error("ERROR: Critical system error during customer creation", e);
      throw new ServiceException("Internal system error while creating customer", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponse getById(String id) {
    logger.info("INFO: Entering getById(String) method. ID: {}", id);

    return customerRepository.findById(id)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warn("WARNING: Customer not found with ID: {}", id);
          return new NotFoundException("Customer", id);
        });
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CustomerResponse> getAll(int page, int size) {
    logger.info("INFO: Entering getAll(page={}, size={}) method using Spring Data JPA.", page, size);

    org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);

    org.springframework.data.domain.Page<Customer> customerPage = customerRepository.findAll(pageable);

    logger.debug("DEBUG: Retrieved {} customers. Total elements: {}",
        customerPage.getNumberOfElements(), customerPage.getTotalElements());

    List<CustomerResponse> content = customerPage.getContent().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());

    return new PageResponse<>(
        content,
        (int) customerPage.getTotalElements(),
        customerPage.getTotalPages(),
        page,
        size,
        customerPage.hasNext(),
        customerPage.hasPrevious()
    );
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponse getByPhone(String phone) {
    logger.info("INFO: Entering getByPhone(String) method. Phone: {}", phone);

    return customerRepository.findByPhone(phone)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warn("WARNING: Customer not found with phone: {}", phone);
          return new NotFoundException("Customer not found with phone: " + phone);
        });
  }
  @Override
  public Optional<Customer> findById(String id) {
    return customerRepository.findById(id);
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
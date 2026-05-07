package com.khanh.ordermanagement.service.customer.impl;

import com.khanh.ordermanagement.dto.request.CustomerRequest;
import com.khanh.ordermanagement.dto.response.CustomerResponse;
import com.khanh.ordermanagement.exception.NotFoundException;
import com.khanh.ordermanagement.exception.database.DuplicateRecordException;
import com.khanh.ordermanagement.exception.database.ServiceException;
import com.khanh.ordermanagement.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.khanh.ordermanagement.dao.CustomerDAO;
import com.khanh.ordermanagement.service.customer.CustomerService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Hoàn chỉnh theo chuẩn Spring Boot REST API
 * Loại bỏ quản lý Connection thủ công, thay bằng @Transactional
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  // Rule: Dùng SLF4J để ghi log 4 mức chuẩn công nghiệp
  private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

  private final CustomerDAO customerDAO;

  public CustomerServiceImpl(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }

  @Override
  @Transactional // Tự động mở/đóng/rollback transaction
  public CustomerResponse create(CustomerRequest request) {
    logger.info("INFO: Entering create(CustomerRequest) method for: {}", request.getName());

    // 1. Validation logic từ DTO
    request.validate();

    // 2. Check trùng số điện thoại - Log DEBUG để truy vết
    logger.debug("DEBUG: Checking if phone {} already exists", request.getPhone());
    customerDAO.findByPhone(request.getPhone()).ifPresent(c -> {
      logger.warn("WARNING: Phone number {} is already registered", request.getPhone());
      throw new DuplicateRecordException("Phone number already registered.");
    });

    try {
      // 3. Mapping sang Entity
      Customer customer = new Customer(
          UUID.randomUUID().toString(),
          request.getName(),
          request.getPhone()
      );

      // 4. Lưu vào DB thông qua DAO (DAO giờ đã dùng JdbcTemplate)
      Customer savedCustomer = customerDAO.save(customer);

      logger.info("INFO: Customer created successfully with ID: {}", savedCustomer.getId());
      return mapToResponse(savedCustomer);

    } catch (Exception e) {
      // Log ERROR kèm StackTrace để debug hệ thống
      logger.error("ERROR: Critical system error during customer creation", e);
      throw new ServiceException("Internal system error while creating customer", e);
    }
  }

  @Override
  @Transactional(readOnly = true) // Tối ưu cho các truy vấn chỉ đọc
  public CustomerResponse getById(String id) {
    logger.info("INFO: Entering getById(String) method. ID: {}", id);

    return customerDAO.findById(id)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warn("WARNING: Customer not found with ID: {}", id);
          return new NotFoundException("Customer", id);
        });
  }

  @Override
  @Transactional(readOnly = true)
  public List<CustomerResponse> getAll() {
    logger.info("INFO: Entering getAll() method.");

    List<Customer> customers = customerDAO.findAll();
    logger.debug("DEBUG: Retrieved {} customers from database.", customers.size());

    return customers.stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponse getByPhone(String phone) {
    logger.info("INFO: Entering getByPhone(String) method. Phone: {}", phone);

    return customerDAO.findByPhone(phone)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warn("WARNING: Customer not found with phone: {}", phone);
          return new NotFoundException("Customer not found with phone: " + phone);
        });
  }

  /**
   * Helper method để map Entity sang DTO
   */
  private CustomerResponse mapToResponse(Customer customer) {
    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getPhone(),
        customer.getCreatedAt()
    );
  }
}
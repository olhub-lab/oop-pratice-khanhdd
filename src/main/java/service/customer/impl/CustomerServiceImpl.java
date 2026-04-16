package service.customer.impl;

import config.DBConnection;
import config.DatabaseUtil;
import dao.CustomerDAO;
import dto.request.CustomerRequest;
import dto.response.CustomerResponse;
import exception.NotFoundException;
import exception.database.DuplicateRecordException;
import exception.database.ServiceException;
import model.Customer;
import service.customer.CustomerService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());
  private final CustomerDAO customerDAO;

  public CustomerServiceImpl(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }

  @Override
  public CustomerResponse create(CustomerRequest request) {
    logger.info("Starting customer creation: " + request.getName());
    request.validate();

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();

      if (customerDAO.findByPhone(request.getPhone()).isPresent()) {
        logger.warning("Customer creation aborted: Phone already exists.");
        throw new DuplicateRecordException("Phone number already registered.");
      }

      DatabaseUtil.beginTransaction(conn);

      Customer customer = new Customer(
          UUID.randomUUID().toString(),
          request.getName(),
          request.getPhone()
      );

      Customer savedCustomer = customerDAO.save(conn, customer);

      DatabaseUtil.commitTransaction(conn);
      logger.info("Customer created successfully with ID: " + savedCustomer.getId());

      return mapToResponse(savedCustomer);

    } catch (Exception e) {
      DatabaseUtil.rollbackTransaction(conn);
      logger.severe("Customer creation failed: " + e.getMessage());
      throw new ServiceException("System error during customer creation", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public CustomerResponse getById(String id) {
    logger.info("Querying customer by ID: " + id);
    return customerDAO.findById(id)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warning("Customer not found with ID: " + id);
          return new NotFoundException("Customer", id);
        });
  }

  @Override
  public List<CustomerResponse> getAll() {
    logger.info("Querying all customers.");
    return customerDAO.findAll().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public CustomerResponse getByPhone(String phone) {
    logger.info("Querying customer by phone: " + phone);
    return customerDAO.findByPhone(phone)
        .map(this::mapToResponse)
        .orElseThrow(() -> {
          logger.warning("Customer not found with phone: " + phone);
          return new NotFoundException("Customer not found with phone: " + phone);
        });
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
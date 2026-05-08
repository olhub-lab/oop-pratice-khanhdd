package com.khanh.ordermanagement.dao.impl;

import com.khanh.ordermanagement.dao.CustomerDAO;
import com.khanh.ordermanagement.exception.database.DatabaseException;
import com.khanh.ordermanagement.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

  private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

  private final JdbcTemplate jdbcTemplate;

  public CustomerDAOImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Customer save(Customer customer) {
    logger.info("INFO: Entering save(Customer) method for: {}", customer.getName());
    String sql = "INSERT INTO customers (id, name, phone, created_at) VALUES (?, ?, ?, ?)";

    logger.debug("DEBUG: Executing SQL: {}", sql);
    try {
      jdbcTemplate.update(
          sql,
          customer.getId(),
          customer.getName(),
          customer.getPhone(),
          Timestamp.valueOf(customer.getCreatedAt())
      );
      logger.info("INFO: Customer data saved to database successfully.");
      return customer;
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to save customer: {}", customer.getName(), e);
      throw new DatabaseException("Database error during save", e);
    }
  }

  @Override
  public Optional<Customer> findById(String id) {
    logger.info("INFO: Entering findById(String) method. ID: {}", id);
    String sql = "SELECT * FROM customers WHERE id = ?";

    logger.debug("DEBUG: Executing SQL: {}", sql);
    try {
      Customer customer = jdbcTemplate.queryForObject(sql, customerRowMapper(), id);
      return Optional.ofNullable(customer);
    } catch (EmptyResultDataAccessException e) {
      logger.warn("WARNING: No customer found with ID: {}", id);
      return Optional.empty();
    } catch (DataAccessException e) {
      logger.error("ERROR: Error finding customer by ID: {}", id, e);
      throw new DatabaseException("Database access error", e);
    }
  }

  @Override
  public Optional<Customer> findByPhone(String phone) {
    logger.info("INFO: Entering findByPhone(String) method. Phone: {}", phone);
    String sql = "SELECT * FROM customers WHERE phone = ?";

    logger.debug("DEBUG: Executing SQL: {}", sql);
    try {
      Customer customer = jdbcTemplate.queryForObject(sql, customerRowMapper(), phone);
      return Optional.ofNullable(customer);
    } catch (EmptyResultDataAccessException e) {
      logger.warn("WARNING: No customer found with phone: {}", phone);
      return Optional.empty();
    } catch (DataAccessException e) {
      logger.error("ERROR: Error finding customer by phone: {}", phone, e);
      throw new DatabaseException("Database access error", e);
    }
  }

  @Override
  public List<Customer> findAll(int page, int size) {
    int offset = page * size;
    logger.info("INFO: Entering findAll() with page: {}, size: {}", page, size);

    String sql = "SELECT * FROM customers LIMIT ? OFFSET ?";

    logger.debug("DEBUG: Executing SQL: {} | Params: [{}, {}]", sql, size, offset);
    try {
      List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper(), size, offset);

      logger.info("INFO: Successfully fetched {} customers for page {}.", customers.size(), page);
      return customers;
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to fetch paginated customers", e);
      throw new DatabaseException("Database error during findAll with pagination", e);
    }

  }
  @Override
  public int count() {
    logger.info("INFO: Counting total customers in database.");
    String sql = "SELECT COUNT(*) FROM customers";

    try {
      Integer total = jdbcTemplate.queryForObject(sql, Integer.class);
      return total != null ? total : 0;
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to count customers", e);
      throw new DatabaseException("Database error during count", e);
    }
  }

  private RowMapper<Customer> customerRowMapper() {
    return (rs, rowNum) -> new Customer(
        rs.getString("id"),
        rs.getString("name"),
        rs.getString("phone"),
        rs.getTimestamp("created_at").toLocalDateTime()
    );
  }
}
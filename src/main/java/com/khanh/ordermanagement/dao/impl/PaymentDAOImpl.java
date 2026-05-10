package com.khanh.ordermanagement.dao.impl;

import com.khanh.ordermanagement.dao.PaymentDAO;
import com.khanh.ordermanagement.model.Payment;
import com.khanh.ordermanagement.model.enums.PaymentMethod;
import com.khanh.ordermanagement.model.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class PaymentDAOImpl implements PaymentDAO {

  private static final Logger logger = LoggerFactory.getLogger(PaymentDAOImpl.class);
  private final JdbcTemplate jdbcTemplate;

  public PaymentDAOImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Payment save(Payment payment) {
    String sql = "INSERT INTO payments (id, order_id, final_amount, payment_method, created_at, status) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    try {
      jdbcTemplate.update(
          sql,
          payment.getPaymentId(),
          payment.getOrderId(),
          payment.getFinalAmount(),
          payment.getPaymentMethod().name(),
          Timestamp.valueOf(payment.getCreatedAt()),
          payment.getStatus().name()
      );
      logger.info("INFO: Payment {} saved successfully for Order {}", payment.getPaymentId(), payment.getOrderId());
      return payment;
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to save payment for order {}", payment.getOrderId(), e);
      throw new RuntimeException("Database save error", e);
    }
  }

  @Override
  public Optional<Payment> findById(String id) {
    String sql = "SELECT * FROM payments WHERE id = ?";
    try {
      Payment payment = jdbcTemplate.queryForObject(sql, paymentRowMapper(), id);
      return Optional.ofNullable(payment);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Payment> findByOrderId(String orderId) {
    String sql = "SELECT * FROM payments WHERE order_id = ?";
    try {
      Payment payment = jdbcTemplate.queryForObject(sql, paymentRowMapper(), orderId);
      return Optional.ofNullable(payment);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void update(Payment payment) {
    String sql = "UPDATE payments SET status = ? WHERE id = ?";
    try {
      jdbcTemplate.update(sql, payment.getStatus().name(), payment.getPaymentId());
      logger.info("INFO: Payment {} status updated to {}", payment.getPaymentId(), payment.getStatus());
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to update payment {}", payment.getPaymentId(), e);
      throw new RuntimeException("Database update error", e);
    }
  }

  private RowMapper<Payment> paymentRowMapper() {
    return (rs, rowNum) -> new Payment(
        rs.getString("id"),
        rs.getString("order_id"),
        rs.getBigDecimal("final_amount"),
        PaymentMethod.valueOf(rs.getString("payment_method")),
        rs.getTimestamp("created_at").toLocalDateTime(),
        PaymentStatus.valueOf(rs.getString("status"))
    );
  }
}
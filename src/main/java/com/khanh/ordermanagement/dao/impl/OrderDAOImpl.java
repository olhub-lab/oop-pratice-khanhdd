package com.khanh.ordermanagement.dao.impl;

import com.khanh.ordermanagement.dao.OrderDAO;
import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
import com.khanh.ordermanagement.exception.database.ServiceException;
import com.khanh.ordermanagement.model.Order;
import com.khanh.ordermanagement.model.enums.OrderStatus;
import com.khanh.ordermanagement.model.enums.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {

  private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);
  private final JdbcTemplate jdbcTemplate;

  public OrderDAOImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Order save(Order order) {
    String sql = "INSERT INTO orders (id, customer_id, customer_name, amount, fee_amount, "
        + "discount_amount, final_amount, status, payment_method, created_at, updated_at) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try {
      jdbcTemplate.update(sql, order.getId(), order.getCustomerId(), order.getCustomerName(),
          order.getAmount(), order.getFeeAmount(), order.getDiscountAmount(),
          order.getFinalAmount(), order.getStatus().name(), order.getPaymentMethod().name(),
          Timestamp.valueOf(order.getCreatedAt()), Timestamp.valueOf(order.getUpdatedAt()));
      return order;
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to save order {}", order.getId(), e);
      throw new ServiceException("Database save error", e);
    }
  }

  @Override
  public void update(Order order) {
    String sql = "UPDATE orders SET status = ?, updated_at = ?, cancel_reason = ? WHERE id = ?";
    try {
      jdbcTemplate.update(sql, order.getStatus().name(), Timestamp.valueOf(order.getUpdatedAt()),
          order.getCancelReason(), order.getId());
    } catch (DataAccessException e) {
      logger.error("ERROR: Failed to update order {}", order.getId(), e);
      throw new ServiceException("Database update error", e);
    }
  }

  @Override
  public Optional<Order> findById(String id) {
    String sql = "SELECT * FROM orders WHERE id = ?";
    try {
      Order order = jdbcTemplate.queryForObject(sql, orderRowMapper(), id);
      return Optional.ofNullable(order);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    } catch (DataAccessException e) {
      logger.error("ERROR: FindById failed for ID: {}", id, e);
      throw new ServiceException("Database access error", e);
    }
  }

  @Override
  public List<Order> findAll(OrderFilterRequest filter) {
    int pageSize = (filter.getSize() == null || filter.getSize() <= 0) ? 10 : filter.getSize();
    int pageNumber = (filter.getPage() == null || filter.getPage() < 0) ? 0 : filter.getPage();
    int offset = pageNumber * pageSize;
    StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
    List<Object> params = new ArrayList<>();

    buildWhereClause(sql, params, filter);

    sql.append(" ORDER BY created_at DESC");
    sql.append(" LIMIT ? OFFSET ?");

    params.add(pageSize);
    params.add(offset);

    return jdbcTemplate.query(sql.toString(), orderRowMapper(), params.toArray());
  }

  @Override
  public int count(OrderFilterRequest filter) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders WHERE 1=1");
    List<Object> params = new ArrayList<>();

    buildWhereClause(sql, params, filter);

    return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
  }

  private void buildWhereClause(StringBuilder sql, List<Object> params, OrderFilterRequest filter) {
    if (filter.getCustomerId() != null) {
      sql.append(" AND customer_id = ?");
      params.add(filter.getCustomerId());
    }
    if (filter.getStatus() != null) {
      sql.append(" AND status = ?");
      params.add(filter.getStatus().name());
    }
    if (filter.getFromDate() != null) {
      sql.append(" AND created_at >= ?");
      params.add(Timestamp.valueOf(filter.getFromDate()));
    }
    if (filter.getToDate() != null) {
      sql.append(" AND created_at <= ?");
      params.add(Timestamp.valueOf(filter.getToDate()));
    }
  }

  private RowMapper<Order> orderRowMapper() {
    return (rs, rowNum) -> {
      Order order = new Order();
      order.setId(rs.getString("id"));
      order.setCustomerId(rs.getString("customer_id"));
      order.setCustomerName(rs.getString("customer_name"));
      order.setAmount(rs.getBigDecimal("amount"));
      order.setFeeAmount(rs.getBigDecimal("fee_amount"));
      order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
      order.setFinalAmount(rs.getBigDecimal("final_amount"));
      order.setStatus(OrderStatus.valueOf(rs.getString("status")));
      order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
      order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
      order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
      order.setCancelReason(rs.getString("cancel_reason"));
      return order;
    };
  }
}
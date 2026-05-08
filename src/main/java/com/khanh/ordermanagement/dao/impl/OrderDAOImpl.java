package com.khanh.ordermanagement.dao.impl;

import com.khanh.ordermanagement.config.DBConnection;
import com.khanh.ordermanagement.dao.OrderDAO;
import com.khanh.ordermanagement.dto.request.OrderFilterRequest;
import com.khanh.ordermanagement.exception.database.ServiceException;
import com.khanh.ordermanagement.model.Order;
import com.khanh.ordermanagement.model.Customer;
import com.khanh.ordermanagement.model.enums.OrderStatus;
import com.khanh.ordermanagement.model.enums.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

  private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

  @Override
  public Order save(Connection conn, Order order) {
    logger.info("INFO: Entering save(Order) method.");
    String sql = "INSERT INTO orders ("
        + "id,"
        + " customer_id,"
        + " customer_name,"
        + " amount, fee_amount,"
        + " discount_amount, "
        + "final_amount, status, "
        + "payment_method,"
        + " created_at, "
        + "updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    logger.debug("DEBUG: Executing SQL: {}", sql);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, order.getId());
      ps.setString(2, order.getCustomerId());
      ps.setString(3, order.getCustomerName());
      ps.setBigDecimal(4, order.getAmount());
      ps.setBigDecimal(5, order.getFeeAmount());
      ps.setBigDecimal(6, order.getDiscountAmount());
      ps.setBigDecimal(7, order.getFinalAmount());
      ps.setString(8, order.getStatus().name());
      ps.setString(9, order.getPaymentMethod().name());
      ps.setTimestamp(10, Timestamp.valueOf(order.getCreatedAt()));
      ps.setTimestamp(11, Timestamp.valueOf(order.getUpdatedAt()));

      ps.executeUpdate();
      logger.info("INFO: Order record {} saved successfully.", order.getId());
      return order;
    } catch (SQLException e) {
      logger.error("ERROR: Failed to save order {}", order.getId(), e);
      throw new ServiceException("Database save error", e);
    }
  }

  @Override
  public void update(Connection conn, Order order) {
    logger.info("INFO: Entering update(Order) method.");
    String sql = "UPDATE orders SET status = ?, updated_at = ?, cancel_reason = ? WHERE id = ?";

    logger.debug("DEBUG: Executing SQL update: {}", sql);
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, order.getStatus().name());
      ps.setTimestamp(2, Timestamp.valueOf(order.getUpdatedAt()));
      ps.setString(3, order.getCancelReason());
      ps.setString(4, order.getId());

      ps.executeUpdate();
      logger.info("INFO: Order {} updated successfully.", order.getId());
    } catch (SQLException e) {
      logger.error("ERROR: Failed to update order {}", order.getId(), e);
      throw new ServiceException("Database update error", e);
    }
  }

  @Override
  public Optional<Order> findById(String id) {
    logger.info("INFO: Entering findById(String) method.");
    String sql = "SELECT * FROM orders WHERE id = ?";

    try (Connection conn =
        DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      logger.error("ERROR: FindById failed for ID: {}", id, e);
    }
    return Optional.empty();
  }

  @Override
  public List<Order> findAll(OrderFilterRequest filter) {
    logger.info("INFO: Entering findAll() method.");
    List<Order> orders = new ArrayList<>();
    String sql = "SELECT * FROM orders";

    try (Connection conn =
        DBConnection.getInstance().getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(
        sql)) {
      while (rs.next()) {
        orders.add(mapToEntity(rs));
      }
      logger.info("INFO: Retrieved {} records.", orders.size());
    } catch (SQLException e) {
      logger.error("ERROR: FindAll failed", e);
    }
    return orders;
  }

  private Order mapToEntity(ResultSet rs) throws SQLException {
    Customer customer = new Customer(
        rs.getString("customer_id"),
        rs.getString("customer_name"),
        "");
    Order order = new Order
        (customer, rs.getBigDecimal("amount"),
        PaymentMethod.valueOf(rs.getString("payment_method")));
    try {
      setField(order, "id", rs.getString("id"));
      setField(order, "status", OrderStatus.valueOf(rs.getString("status")));
      setField(order, "feeAmount", rs.getBigDecimal("fee_amount"));
      setField(order, "discountAmount", rs.getBigDecimal("discount_amount"));
      setField(order, "finalAmount", rs.getBigDecimal("final_amount"));
      setField(order, "createdAt", rs.getTimestamp("created_at").toLocalDateTime());
      setField(order, "updatedAt", rs.getTimestamp("updated_at").toLocalDateTime());
      setField(order, "cancelReason", rs.getString("cancel_reason"));
    } catch (Exception e) {
      logger.error("ERROR: Mapping failed", e);
    }
    return order;
  }

  private void setField(Object obj, String name, Object val) throws Exception {
    Field field = obj.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.set(obj, val);
  }
}
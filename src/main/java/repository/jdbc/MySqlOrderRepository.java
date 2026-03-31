package repository.jdbc;

import config.DBConnection;
import exception.database.DatabaseException;
import model.Order;
import model.enums.OrderStatus;
import model.enums.PaymentMethod;
import repository.OrderRepository;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MySqlOrderRepository implements OrderRepository {

  private static final Logger logger = Logger.getLogger(MySqlOrderRepository.class.getName());

  private static final String SQL_INSERT =
      "INSERT INTO orders (order_id, customer_id, customer_name, amount, fee_amount, "
          + "discount_amount, final_amount, payment_method, status, created_at, updated_at, cancel_reason) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_FIND_BY_ID = "SELECT * FROM orders WHERE order_id = ?";

  private static final String SQL_FIND_ALL = "SELECT * FROM orders";

  private static final String SQL_UPDATE = "UPDATE orders SET status = ?, cancel_reason = ?, updated_at = ? WHERE order_id = ?";

  private final DBConnection dbConnection;

  public MySqlOrderRepository(DBConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  @Override
  public void save(Order order) {
    logger.info(() -> "Saving order to DB: " + order.getOrderId());

    try (Connection conn = dbConnection.getMysqlConnection()) {
      DatabaseUtil.beginTransaction(conn);

      try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
        ps.setString(1, order.getOrderId());
        ps.setLong(2, order.getCustomerId());
        ps.setString(3, order.getCustomerName());
        ps.setBigDecimal(4, order.getAmount());
        ps.setBigDecimal(5, order.getFeeAmount());
        ps.setBigDecimal(6, order.getDiscountAmount());
        ps.setBigDecimal(7, order.getFinalAmount());
        ps.setString(8, order.getPaymentMethod().name());
        ps.setString(9, order.getStatus().name());
        ps.setTimestamp(10, Timestamp.valueOf(order.getCreatedAt()));
        ps.setTimestamp(11, Timestamp.valueOf(order.getUpdatedAt()));
        ps.setString(12, order.getCancelReason());

        ps.executeUpdate();
        DatabaseUtil.commitTransaction(conn);

        logger.info(() -> "Order saved successfully: " + order.getOrderId());

      } catch (SQLException e) {
        DatabaseUtil.rollback(conn);
        throw new DatabaseException("Failed to save order: " + order.getOrderId(), e);
      }

    } catch (SQLException e) {
      throw new DatabaseException("Failed to get connection", e);
    }
  }

  @Override
  public Optional<Order> findById(String orderId) {
    logger.info(() -> "Finding order by id: " + orderId);

    try (Connection conn = dbConnection.getMysqlConnection(); PreparedStatement ps = conn.prepareStatement(
        SQL_FIND_BY_ID)) {

      ps.setString(1, orderId);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(this.mapToOrder(rs));
        }
        return Optional.empty();
      }

    } catch (SQLException e) {
      throw new DatabaseException("Failed to find order: " + orderId, e);
    }
  }

  @Override
  public List<Order> findAll() {
    logger.info(() -> "Finding all orders from DB");

    final List<Order> orders = new ArrayList<>();

    try (Connection conn = dbConnection.getMysqlConnection(); PreparedStatement ps = conn.prepareStatement(
        SQL_FIND_ALL); ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        orders.add(this.mapToOrder(rs));
      }

      logger.info(() -> "Found " + orders.size() + " orders");
      return orders;

    } catch (SQLException e) {
      throw new DatabaseException("Failed to fetch all orders", e);
    }
  }

  @Override
  public void update(Order order) {
    logger.info(() -> "Updating order in DB: " + order.getOrderId());

    try (Connection conn = dbConnection.getMysqlConnection()) {
      DatabaseUtil.beginTransaction(conn);

      try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
        ps.setString(1, order.getStatus().name());
        ps.setString(2, order.getCancelReason());
        ps.setTimestamp(3, Timestamp.valueOf(order.getUpdatedAt()));
        ps.setString(4, order.getOrderId());

        ps.executeUpdate();
        DatabaseUtil.commitTransaction(conn);

        logger.info(() -> "Order updated successfully: " + order.getOrderId());

      } catch (SQLException e) {
        DatabaseUtil.rollback(conn);
        throw new DatabaseException("Failed to update order: " + order.getOrderId(), e);
      }

    } catch (SQLException e) {
      throw new DatabaseException("Failed to get connection", e);
    }
  }

  private Order mapToOrder(ResultSet rs) throws SQLException {
    return new Order.Builder().orderId(rs.getString("order_id"))
        .customerId(rs.getLong("customer_id")).customerName(rs.getString("customer_name"))
        .amount(rs.getBigDecimal("amount")).feeAmount(rs.getBigDecimal("fee_amount"))
        .discountAmount(rs.getBigDecimal("discount_amount"))
        .finalAmount(rs.getBigDecimal("final_amount"))
        .paymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")))
        .status(OrderStatus.valueOf(rs.getString("status")))
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
        .cancelReason(rs.getString("cancel_reason")).build();
  }
}
package dao.impl;

import config.DBConnection;
import config.DatabaseUtil;
import dao.CustomerDAO;
import exception.database.DatabaseException;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAOImpl implements CustomerDAO {

  private static final Logger logger = Logger.getLogger(CustomerDAOImpl.class.getName());

  @Override
  public Customer save(Connection conn, Customer customer) {
    logger.info(() -> "Đang thực hiện lưu thông tin khách hàng: " + customer.getName());
    String sql = "INSERT INTO customers (id, name, phone, created_at) VALUES (?, ?, ?, ?)";

    if (logger.isLoggable(Level.FINE)) {
      logger.fine(() -> "Thực thi SQL: " + sql + " [ID: " + customer.getId() + "]");
    }

    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, customer.getId());
      ps.setString(2, customer.getName());
      ps.setString(3, customer.getPhone());
      ps.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));

      ps.executeUpdate();
      logger.info(() -> "Lưu thành công khách hàng vào cơ sở dữ liệu.");
      return customer;
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi khi thực hiện lệnh SQL lưu khách hàng: " + customer.getName(), e);
      throw new DatabaseException("Lỗi khi lưu khách hàng vào DB: " + customer.getName(), e);
    } finally {
      DatabaseUtil.close(ps);
    }
  }

  @Override
  public Optional<Customer> findById(String id) {
    logger.fine(() -> "Đang tìm kiếm khách hàng theo ID: " + id);
    String sql = "SELECT * FROM customers WHERE id = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          logger.fine(() -> "Đã tìm thấy khách hàng ID: " + id);
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi truy vấn tìm khách hàng theo ID: " + id, e);
      throw new DatabaseException("Lỗi khi tìm khách hàng theo ID: " + id, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Customer> findByPhone(String phone) {
    logger.fine(() -> "Đang tìm kiếm khách hàng theo số điện thoại: " + phone);
    String sql = "SELECT * FROM customers WHERE phone = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, phone);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          logger.fine(() -> "Đã tìm thấy khách hàng có số điện thoại: " + phone);
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi truy vấn theo số điện thoại: " + phone, e);
      throw new DatabaseException("Lỗi khi tìm khách hàng theo số điện thoại: " + phone, e);
    }
    return Optional.empty();
  }

  @Override
  public List<Customer> findAll() {
    logger.info(() -> "Đang lấy danh sách toàn bộ khách hàng từ cơ sở dữ liệu.");
    String sql = "SELECT * FROM customers";
    List<Customer> customers = new ArrayList<>();

    try (Connection conn = DBConnection.getInstance().getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {

      while (rs.next()) {
        customers.add(mapToEntity(rs));
      }
      logger.info(() -> "Lấy danh sách khách hàng thành công. Tổng số: " + customers.size());
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi khi lấy danh sách khách hàng", e);
      throw new DatabaseException("Lỗi khi lấy danh sách khách hàng", e);
    }
    return customers;
  }

  private Customer mapToEntity(ResultSet rs) throws SQLException {
    return new Customer(
        rs.getString("id"),
        rs.getString("name"),
        rs.getString("phone"),
        rs.getTimestamp("created_at").toLocalDateTime()
    );
  }
}
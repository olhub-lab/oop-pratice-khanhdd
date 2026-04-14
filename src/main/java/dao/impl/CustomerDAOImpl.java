package dao.impl;

import config.DBConnection;
import dao.CustomerDAO;
import exception.database.DatabaseException;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

  @Override
  public Customer save(Customer customer) {
    String sql = "INSERT INTO customers (id, name, phone, created_at) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, customer.getId());
      ps.setString(2, customer.getName());
      ps.setString(3, customer.getPhone());
      ps.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));

      ps.executeUpdate();
      return customer;
    } catch (SQLException e) {
      throw new DatabaseException("Lỗi khi lưu khách hàng: " + customer.getName(), e);
    }
  }

  @Override
  public Optional<Customer> findById(String id) {
    String sql = "SELECT * FROM customers WHERE id = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Lỗi khi tìm khách hàng theo ID: " + id, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Customer> findByPhone(String phone) {
    String sql = "SELECT * FROM customers WHERE phone = ?";

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, phone);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Lỗi khi tìm khách hàng theo số điện thoại: " + phone, e);
    }
    return Optional.empty();
  }

  @Override
  public List<Customer> findAll() {
    String sql = "SELECT * FROM customers";
    List<Customer> customers = new ArrayList<>();

    try (Connection conn = DBConnection.getInstance().getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {

      while (rs.next()) {
        customers.add(mapToEntity(rs));
      }
    } catch (SQLException e) {
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
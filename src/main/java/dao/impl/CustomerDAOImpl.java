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
    String sql = "INSERT INTO customers (id, name, phone, created_at) VALUES (?, ?, ?, ?)";
    logger.fine("Executing SQL: " + sql);

    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, customer.getId());
      ps.setString(2, customer.getName());
      ps.setString(3, customer.getPhone());
      ps.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));

      ps.executeUpdate();
      logger.info("Customer data saved to database.");
      return customer;
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Error executing save customer: " + customer.getName(), e);
      throw new DatabaseException("Database error during save", e);
    } finally {
      DatabaseUtil.close(ps);
    }
  }

  @Override
  public Optional<Customer> findById(String id) {
    String sql = "SELECT * FROM customers WHERE id = ?";
    logger.fine("Executing SQL: " + sql);

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error finding customer by ID: " + id, e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Customer> findByPhone(String phone) {
    String sql = "SELECT * FROM customers WHERE phone = ?";
    logger.fine("Executing SQL: " + sql);

    try (Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, phone);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapToEntity(rs));
        }
      }
    } catch (SQLException e) {
      throw new DatabaseException("Error finding customer by phone: " + phone, e);
    }
    return Optional.empty();
  }

  @Override
  public List<Customer> findAll() {
    String sql = "SELECT * FROM customers";
    logger.info("Fetching all customers from database.");
    List<Customer> customers = new ArrayList<>();

    try (Connection conn = DBConnection.getInstance().getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {

      while (rs.next()) {
        customers.add(mapToEntity(rs));
      }
      return customers;
    } catch (SQLException e) {
      throw new DatabaseException("Error fetching all customers", e);
    }
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
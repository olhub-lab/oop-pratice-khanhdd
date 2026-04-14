package service.customer.impl;

import config.DBConnection;
import config.DatabaseUtil;
import dao.CustomerDAO;
import dto.request.CustomerRequest;
import dto.response.CustomerResponse;
import exception.NotFoundException;
import exception.database.DatabaseException;
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
    logger.info(() -> "Bắt đầu quy trình tạo mới khách hàng: " + request.getName());
    request.validate();

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      DatabaseUtil.beginTransaction(conn);

      Customer customer = new Customer(UUID.randomUUID().toString(), request.getName(),
          request.getPhone());
      Customer savedCustomer = customerDAO.save(conn, customer);

      DatabaseUtil.commitTransaction(conn);
      logger.info(() -> "Tạo khách hàng thành công. ID hệ thống: " + savedCustomer.getId());

      return mapToResponse(savedCustomer);

    } catch (Exception e) {
      DatabaseUtil.rollbackTransaction(conn);
      logger.severe(() -> "Quy trình tạo khách hàng thất bại, đã hoàn tác dữ liệu: " + e.getMessage());
      throw new DatabaseException("Lỗi hệ thống khi truy vấn dữ liệu", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public CustomerResponse getById(String id) {
    logger.info(() -> "Đang truy vấn thông tin khách hàng theo ID: " + id);
    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      return customerDAO.findById(id).map(this::mapToResponse).orElseThrow(() -> {
        logger.warning("Không tìm thấy khách hàng với ID: " + id);
        return new NotFoundException("Customer", id);
      });
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi kết nối khi tìm khách hàng theo ID", e);
      throw new DatabaseException("Lỗi hệ thống khi truy vấn dữ liệu", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public List<CustomerResponse> getAll() {
    logger.info(() -> "Đang lấy danh sách toàn bộ khách hàng.");

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      return customerDAO.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi kết nối khi lấy danh sách khách hàng", e);
      throw new DatabaseException("Lỗi hệ thống khi truy vấn dữ liệu", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  @Override
  public CustomerResponse getByPhone(String phone) {
    logger.info(() ->"Đang truy vấn khách hàng theo số điện thoại: " + phone);

    Connection conn = null;
    try {
      conn = DBConnection.getInstance().getConnection();
      return customerDAO.findByPhone(phone).map(this::mapToResponse).orElseThrow(() -> {
        logger.warning(() -> "Không tìm thấy số điện thoại: " + phone);
        return new NotFoundException("Không tìm thấy khách hàng với số điện thoại: " + phone);
      });
    } catch (SQLException e) {
      logger.log(Level.SEVERE, "Lỗi kết nối khi tìm khách hàng theo số điện thoại", e);
      throw new DatabaseException("Lỗi hệ thống khi truy vấn dữ liệu", e);
    } finally {
      DatabaseUtil.close(conn);
    }
  }

  private CustomerResponse mapToResponse(Customer customer) {
    return new CustomerResponse(
        customer.getId(),
        customer.getName(),
        customer.getPhone(),
        customer.getCreatedAt());
  }
}
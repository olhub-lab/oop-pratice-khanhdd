package dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import model.Customer;

public interface CustomerDAO {

  Customer save(Connection conn,Customer customer);

  Optional<Customer> findById(String id);

  Optional<Customer> findByPhone(String phone);

  List<Customer> findAll();
}
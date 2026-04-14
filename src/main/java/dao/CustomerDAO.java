package dao;

import java.util.List;
import java.util.Optional;
import model.Customer;

public interface CustomerDAO {

  Customer save(Customer customer);

  Optional<Customer> findById(String id);

  Optional<Customer> findByPhone(String phone);

  List<Customer> findAll();
}
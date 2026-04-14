package dao.impl;

import dao.CustomerDAO;
import model.Customer;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

  @Override
  public Customer save(Customer customer) {
    return null;
  }

  @Override
  public Optional<Customer> findById(String id) {
    return Optional.empty();
  }

  @Override
  public Optional<Customer> findByPhone(String phone) {
    return Optional.empty();
  }

  @Override
  public List<Customer> findAll() {
    return null;
  }
}
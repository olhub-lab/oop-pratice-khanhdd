package dao.impl;

import dao.OrderDAO;
import dto.request.OrderFilterRequest;
import model.Order;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

  @Override
  public Order save(Order order) {
    return null;
  }

  @Override
  public Optional<Order> findById(String id) {
    return Optional.empty();
  }

  @Override
  public List<Order> findAll(OrderFilterRequest filter) {
    return null;
  }

  @Override
  public void update(Order order) {
  }
}
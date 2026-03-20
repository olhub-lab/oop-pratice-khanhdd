package repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.Order;
import repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {

  private final Map<String, Order> storage = new HashMap<>();

  @Override
  public void save(Order order) {
    storage.put(order.getOrderId(), order);
  }

  @Override
  public Optional<Order> findById(String orderId) {
    return Optional.ofNullable(storage.get(orderId));
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public void update(Order order) {
    storage.put(order.getOrderId(), order);
  }
}